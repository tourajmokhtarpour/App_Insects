package com.example.insectdetector

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.insectdetector.databinding.ActivityResultBinding
import com.example.insectdetector.detector.YOLODetector
import com.example.insectdetector.utils.DataRecorder
import com.example.insectdetector.utils.LocationHelper
import java.text.DecimalFormat
import java.util.concurrent.Executors

class ResultActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityResultBinding
    private lateinit var detector: YOLODetector
    private lateinit var locationHelper: LocationHelper
    private lateinit var dataRecorder: DataRecorder
    private val executor = Executors.newSingleThreadExecutor()
    
    companion object {
        private const val TAG = "ResultActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        locationHelper = LocationHelper(this)
        dataRecorder = DataRecorder(this)
        
        try {
            detector = YOLODetector(this)
        } catch (e: Exception) {
            Log.e(TAG, "خطا: ${e.message}", e)
            Toast.makeText(this, "خطا: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        
        val imageUri = intent.getStringExtra("image_uri")
        val className = intent.getStringExtra("class_name")
        val confidence = intent.getFloatExtra("confidence", 0f)
        val primaryKey = intent.getStringExtra("primary_key")
        
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        
        if (imageUri != null) {
            try {
                val inputStream = contentResolver.openInputStream(Uri.parse(imageUri))
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                
                if (bitmap != null) {
                    binding.ivInsectImage.setImageBitmap(bitmap)
                    
                    if (className == null || className.isEmpty()) {
                        detectInImage(bitmap)
                    } else {
                        displayResults(className, confidence, latitude, longitude, primaryKey)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "خطا: ${e.message}", e)
            }
        } else if (className != null) {
            displayResults(className, confidence, latitude, longitude, primaryKey)
        }
        
        binding.btnBack.setOnClickListener { finish() }
    }
    
    private fun detectInImage(bitmap: Bitmap) {
        binding.progressBar.isVisible = true
        
        executor.execute {
            try {
                val results = detector.detect(bitmap)
                
                runOnUiThread {
                    binding.progressBar.isVisible = false
                    
                    if (results.isNotEmpty()) {
                        val topResult = results[0]
                        
                        if (topResult.confidence >= 0.50f) {
                            val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
                            val userName = prefs.getString("user_name", "نامشخص") ?: "نامشخص"
                            
                            val latitude = intent.getDoubleExtra("latitude", 0.0)
                            val longitude = intent.getDoubleExtra("longitude", 0.0)
                            
                            val recordResult = dataRecorder.recordDetection(
                                bitmap = bitmap,
                                className = topResult.className,
                                confidence = topResult.confidence,
                                latitude = latitude,
                                longitude = longitude,
                                userName = userName
                            )
                            
                            if (recordResult.success) {
                                Toast.makeText(
                                    this,
                                    "✅ ثبت شد: ${topResult.className}\nکلید: ${recordResult.primaryKey}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            
                            displayResults(
                                topResult.className,
                                topResult.confidence,
                                latitude,
                                longitude,
                                recordResult.primaryKey
                            )
                        } else {
                            Toast.makeText(
                                this,
                                "درصد تشخیص پایین است: ${String.format("%.1f", topResult.confidence * 100)}٪",
                                Toast.LENGTH_LONG
                            ).show()
                            binding.tvClassName.text = "تشخیص پایین: ${String.format("%.1f", topResult.confidence * 100)}٪"
                        }
                    } else {
                        Toast.makeText(this, "حشره‌ای تشخیص داده نشد", Toast.LENGTH_SHORT).show()
                        binding.tvClassName.text = "حشره‌ای تشخیص داده نشد"
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "خطا: ${e.message}", e)
                runOnUiThread {
                    binding.progressBar.isVisible = false
                    Toast.makeText(this, "خطا: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    
    private fun displayResults(
        className: String,
        confidence: Float,
        latitude: Double,
        longitude: Double,
        primaryKey: String?
    ) {
        try {
            val insectInfo = detector.getInsectInfo(className)
            val df = DecimalFormat("#.##")
            val confidencePercent = (confidence * 100)
            
            binding.apply {
                tvClassName.text = insectInfo.name
                tvScientificName.text = insectInfo.scientificName
                tvConfidence.text = "میزان اطمینان: ${df.format(confidencePercent)}٪"
                tvFamily.text = "خانواده: ${insectInfo.family}"
                tvDescription.text = insectInfo.description
                tvHabitat.text = "زیستگاه: ${insectInfo.habitat}"
                tvDiet.text = "تغذیه: ${insectInfo.diet}"
                tvLifecycle.text = "چرخه زندگی: ${insectInfo.lifecycle}"
                tvFacts.text = insectInfo.interestingFacts
                
                // نمایش کلید اصلی
                if (!primaryKey.isNullOrEmpty()) {
                    primaryKeyCard.isVisible = true
                    tvPrimaryKey.text = "🔑 $primaryKey"
                } else {
                    primaryKeyCard.isVisible = false
                }
                
                // نمایش موقعیت مکانی
                if (latitude != 0.0 && longitude != 0.0) {
                    locationCard.isVisible = true
                    tvLocation.text = String.format(
                        "📍 %.6f, %.6f",
                        latitude,
                        longitude
                    )
                    
                    val mapsLink = "https://maps.google.com/?q=$latitude,$longitude"
                    tvMapsLink.setOnClickListener {
                        val intent = android.content.Intent(
                            android.content.Intent.ACTION_VIEW,
                            Uri.parse(mapsLink)
                        )
                        startActivity(intent)
                    }
                } else {
                    locationCard.isVisible = false
                }
                
                if (insectInfo.isDangerous) {
                    warningCard.isVisible = true
                    tvWarning.text = "⚠️ این حشره ممکن است خطرناک باشد"
                } else {
                    warningCard.isVisible = false
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "خطا: ${e.message}", e)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
        detector.close()
    }
}

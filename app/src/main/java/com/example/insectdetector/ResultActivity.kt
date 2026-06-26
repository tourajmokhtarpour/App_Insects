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
import java.text.DecimalFormat
import java.util.concurrent.Executors

class ResultActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityResultBinding
    private lateinit var detector: YOLODetector
    private val executor = Executors.newSingleThreadExecutor()
    
    companion object {
        private const val TAG = "ResultActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
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
                        displayResults(className, confidence)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "خطا: ${e.message}", e)
            }
        } else if (className != null) {
            displayResults(className, confidence)
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
                        displayResults(topResult.className, topResult.confidence)
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
    
    private fun displayResults(className: String, confidence: Float) {
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

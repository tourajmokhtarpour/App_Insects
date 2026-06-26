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
            Log.d(TAG, "در حال ساخت YOLODetector...")
            detector = YOLODetector(this)
            Log.d(TAG, "✅ YOLODetector ساخته شد")
        } catch (e: Exception) {
            Log.e(TAG, "❌ خطا در ساخت YOLODetector: ${e.message}", e)
            Toast.makeText(this, "خطا در بارگذاری مدل: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val imageUri = intent.getStringExtra("image_uri")
        val className = intent.getStringExtra("class_name")
        val confidence = intent.getFloatExtra("confidence", 0f)
        
        Log.d(TAG, "imageUri: $imageUri, className: $className")
        
        if (imageUri != null) {
            try {
                val inputStream = contentResolver.openInputStream(Uri.parse(imageUri))
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                
                if (bitmap != null) {
                    Log.d(TAG, "تصویر بارگذاری شد: ${bitmap.width}x${bitmap.height}")
                    binding.ivInsectImage.setImageBitmap(bitmap)
                    
                    if (className == null || className.isEmpty()) {
                        Log.d(TAG, "شروع تشخیص...")
                        detectInImage(bitmap)
                    } else {
                        Log.d(TAG, "نمایش نتیجه: $className")
                        displayResults(className, confidence)
                    }
                } else {
                    Log.e(TAG, "Bitmap null است")
                    Toast.makeText(this, "خطا در بارگذاری تصویر", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "خطا در بارگذاری تصویر: ${e.message}", e)
                Toast.makeText(this, "خطا: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else if (className != null) {
            displayResults(className, confidence)
        } else {
            Toast.makeText(this, "تصویری دریافت نشد", Toast.LENGTH_SHORT).show()
            finish()
        }
        
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun detectInImage(bitmap: Bitmap) {
        binding.progressBar.isVisible = true
        
        executor.execute {
            try {
                Log.d(TAG, "در حال اجرای detect...")
                val results = detector.detect(bitmap)
                Log.d(TAG, "نتایج: ${results.size}")
                
                runOnUiThread {
                    binding.progressBar.isVisible = false
                    
                    if (results.isNotEmpty()) {
                        val topResult = results[0]
                        Log.d(TAG, "بهترین نتیجه: ${topResult.className} (${topResult.confidence})")
                        displayResults(topResult.className, topResult.confidence)
                    } else {
                        Log.d(TAG, "هیچ حشره‌ای تشخیص داده نشد")
                        Toast.makeText(this, "حشره‌ای تشخیص داده نشد", Toast.LENGTH_SHORT).show()
                        binding.tvClassName.text = "حشره‌ای تشخیص داده نشد"
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "خطا در تشخیص: ${e.message}", e)
                runOnUiThread {
                    binding.progressBar.isVisible = false
                    Toast.makeText(this, "خطا در تشخیص: ${e.message}", Toast.LENGTH_LONG).show()
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
            Log.e(TAG, "خطا در نمایش نتایج: ${e.message}", e)
            Toast.makeText(this, "خطا در نمایش نتایج: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
        try {
            detector.close()
        } catch (e: Exception) {
            Log.e(TAG, "خطا در بستن detector: ${e.message}", e)
        }
    }
}

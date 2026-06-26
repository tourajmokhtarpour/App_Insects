package com.example.insectdetector

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.insectdetector.databinding.ActivityResultBinding
import com.example.insectdetector.detector.YOLODetector
import java.text.DecimalFormat

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var detector: YOLODetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detector = YOLODetector(this)

        val imageUri = intent.getStringExtra("image_uri")
        val className = intent.getStringExtra("class_name")
        val confidence = intent.getFloatExtra("confidence", 0f)
        
        imageUri?.let {
            val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(Uri.parse(it)))
            binding.ivInsectImage.setImageBitmap(bitmap)
        }
        
        if (className != null) {
            displayResults(className, confidence)
        }
        
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun displayResults(className: String, confidence: Float) {
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
    }
}
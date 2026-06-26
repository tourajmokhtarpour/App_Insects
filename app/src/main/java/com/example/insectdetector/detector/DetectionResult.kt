package com.example.insectdetector.detector

import android.graphics.RectF

data class DetectionResult(
    val className: String,
    val confidence: Float,
    val boundingBox: RectF,
    val classId: Int
)

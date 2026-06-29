package com.example.insectdetector.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import com.example.insectdetector.detector.DetectionResult
import java.text.DecimalFormat

class InsectOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val detections = mutableListOf<DetectionResult>()
    private val boxPaint = Paint()
    private val textPaint = Paint()
    private val backgroundPaint = Paint()
    private val formatter = DecimalFormat("#.##")

    // رنگ‌های مختلف برای گونه‌های مختلف
    private val colors = intArrayOf(
        Color.parseColor("#FF5722"),  // نارنجی
        Color.parseColor("#2196F3"),  // آبی
        Color.parseColor("#4CAF50"),  // سبز
        Color.parseColor("#9C27B0"),  // بنفش
        Color.parseColor("#FFC107"),  // زرد
        Color.parseColor("#E91E63"),  // صورتی
        Color.parseColor("#00BCD4"),  // فیروزه‌ای
        Color.parseColor("#FF9800")   // نارنجی روشن
    )

    init {
        // تنظیمات Paint برای کادر
        boxPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 8f
            isAntiAlias = true
        }

        // تنظیمات Paint برای متن
        textPaint.apply {
            color = Color.WHITE
            textSize = 42f
            isAntiAlias = true
            typeface = Typeface.DEFAULT_BOLD
        }

        // تنظیمات Paint برای پس‌زمینه متن
        backgroundPaint.apply {
            color = Color.parseColor("#CC000000") // سیاه نیمه‌شفاف
            style = Paint.Style.FILL
            isAntiAlias = true
        }
    }

    /**
     * تنظیم تشخیص‌ها برای رسم
     */
    fun setDetections(newDetections: List<DetectionResult>) {
        detections.clear()
        detections.addAll(newDetections)
        invalidate() // درخواست رسم مجدد
    }

    /**
     * پاک کردن تشخیص‌ها
     */
    fun clearDetections() {
        detections.clear()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (detections.isEmpty()) return

        for (i in detections.indices) {
            val detection = detections[i]
            val color = colors[i % colors.size]

            // رسم کادر
            boxPaint.color = color
            canvas.drawRect(detection.boundingBox, boxPaint)

            // متن برچسب
            val confidencePercent = formatter.format(detection.confidence * 100)
            val label = "${detection.className} ($confidencePercent%)"

            // اندازه متن
            val textBounds = android.graphics.Rect()
            textPaint.getTextBounds(label, 0, label.length, textBounds)
            val textWidth = textBounds.width()
            val textHeight = textBounds.height()

            // موقعیت برچسب (بالای کادر)
            var labelX = detection.boundingBox.left
            var labelY = detection.boundingBox.top - 10f

            // اگر برچسب از بالا بیرون زد، داخل کادر قرار بده
            if (labelY - textHeight < 0) {
                labelY = detection.boundingBox.top + textHeight + 10f
            }

            // اگر برچسب از راست بیرون زد، تنظیم کن
            if (labelX + textWidth > width) {
                labelX = width - textWidth - 10f
            }

            // پس‌زمینه برچسب
            val padding = 12f
            backgroundPaint.color = color
            val bgRect = RectF(
                labelX - padding,
                labelY - textHeight - padding,
                labelX + textWidth + padding,
                labelY + padding
            )
            canvas.drawRoundRect(bgRect, 12f, 12f, backgroundPaint)

            // رسم متن
            textPaint.color = Color.WHITE
            canvas.drawText(label, labelX, labelY, textPaint)
        }
    }
}

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

    private var imageWidth: Int = 1
    private var imageHeight: Int = 1
    private var isCameraMode: Boolean = true

    private val colors = intArrayOf(
        Color.parseColor("#FF5722"),
        Color.parseColor("#2196F3"),
        Color.parseColor("#4CAF50"),
        Color.parseColor("#9C27B0"),
        Color.parseColor("#FFC107"),
        Color.parseColor("#E91E63"),
        Color.parseColor("#00BCD4"),
        Color.parseColor("#FF9800")
    )

    init {
        boxPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 6f
            isAntiAlias = true
        }

        textPaint.apply {
            color = Color.WHITE
            textSize = 36f
            isAntiAlias = true
            typeface = Typeface.DEFAULT_BOLD
        }

        backgroundPaint.apply {
            style = Paint.Style.FILL
            isAntiAlias = true
        }
    }

    /**
     * تنظیم ابعاد تصویر برای مقیاس‌دهی صحیح
     */
    fun setImageDimensions(width: Int, height: Int) {
        imageWidth = width.coerceAtLeast(1)
        imageHeight = height.coerceAtLeast(1)
    }

    /**
     * تنظیم حالت (دوربین یا گالری)
     */
    fun setCameraMode(isCamera: Boolean) {
        isCameraMode = isCamera
    }

    /**
     * تنظیم تشخیص‌ها برای رسم
     */
    fun setDetections(newDetections: List<DetectionResult>) {
        detections.clear()
        detections.addAll(newDetections)
        invalidate()
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

        val viewWidth = width.toFloat()
        val viewHeight = height.toFloat()

        for (i in detections.indices) {
            val detection = detections[i]
            val color = colors[i % colors.size]

            val scaledBox = scaleBoundingBox(detection.boundingBox, viewWidth, viewHeight)

            boxPaint.color = color
            canvas.drawRect(scaledBox, boxPaint)

            val confidencePercent = formatter.format(detection.confidence * 100)
            val label = "${detection.className} ($confidencePercent%)"

            val textBounds = android.graphics.Rect()
            textPaint.getTextBounds(label, 0, label.length, textBounds)
            val textWidth = textBounds.width()
            val textHeight = textBounds.height()

            var labelX = scaledBox.left
            var labelY = scaledBox.top - 10f

            if (labelY - textHeight < 0) {
                labelY = scaledBox.top + textHeight + 10f
            }

            if (labelX + textWidth > viewWidth) {
                labelX = viewWidth - textWidth - 10f
            }

            val padding = 10f
            backgroundPaint.color = color
            val bgRect = RectF(
                labelX - padding,
                labelY - textHeight - padding,
                labelX + textWidth + padding,
                labelY + padding
            )
            canvas.drawRoundRect(bgRect, 8f, 8f, backgroundPaint)

            textPaint.color = Color.WHITE
            canvas.drawText(label, labelX, labelY, textPaint)
        }
    }

    private fun scaleBoundingBox(originalBox: RectF, viewWidth: Float, viewHeight: Float): RectF {
        return if (isCameraMode) {
            val scaleX = viewWidth / imageWidth
            val scaleY = viewHeight / imageHeight

            RectF(
                originalBox.left * scaleX,
                originalBox.top * scaleY,
                originalBox.right * scaleX,
                originalBox.bottom * scaleY
            )
        } else {
            originalBox
        }
    }
}

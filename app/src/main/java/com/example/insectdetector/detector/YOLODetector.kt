package com.example.insectdetector.detector

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import com.example.insectdetector.data.InsectDatabase
import com.example.insectdetector.utils.Constants
import org.tensorflow.lite.Interpreter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class YOLODetector(private val context: Context) {
    private var interpreter: Interpreter? = null
    private val inputSize = Constants.INPUT_SIZE
    private val confidenceThreshold = Constants.CONFIDENCE_THRESHOLD
    private val classNames = Constants.CLASS_NAMES
    
    private val numClasses = classNames.size
    private val numOutputElements = 8400
    private val outputElementSize = 4 + numClasses

    init {
        loadModel()
    }

    private fun loadModel() {
        try {
            val modelFile = File(context.cacheDir, "best_float16.tflite")
            if (!modelFile.exists()) {
                context.assets.open("best_float16.tflite").use { input ->
                    FileOutputStream(modelFile).use { output ->
                        input.copyTo(output)
                    }
                }
            }
            
            val modelBuffer = loadModelFile(modelFile)
            
            val options = Interpreter.Options().apply {
                setNumThreads(4)
                try {
                    val gpuDelegate = org.tensorflow.lite.gpu.GpuDelegate()
                    addDelegate(gpuDelegate)
                } catch (e: Exception) {
                    // GPU موجود نیست
                }
            }
            
            interpreter = Interpreter(modelBuffer, options)
            println("✅ مدل TFLite با موفقیت بارگذاری شد")
        } catch (e: Exception) {
            e.printStackTrace()
            println("❌ خطا در بارگذاری مدل: ${e.message}")
        }
    }

    private fun loadModelFile(file: File): MappedByteBuffer {
        val fileInputStream = FileInputStream(file)
        val channel = fileInputStream.channel
        return channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size()).also {
            fileInputStream.close()
        }
    }

    fun detect(bitmap: Bitmap): List<DetectionResult> {
        if (interpreter == null) return emptyList()

        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)
        val inputBuffer = bitmapToByteBuffer(resizedBitmap)
        
        val outputBuffer = Array(1) { 
            Array(numOutputElements) { FloatArray(outputElementSize) } 
        }
        
        try {
            interpreter?.run(inputBuffer, outputBuffer)
            return parseDetections(outputBuffer[0], bitmap.width, bitmap.height)
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }

    private fun bitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val buffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3)
        buffer.order(ByteOrder.nativeOrder())
        
        val pixels = IntArray(inputSize * inputSize)
        bitmap.getPixels(pixels, 0, inputSize, 0, 0, inputSize, inputSize)
        
        for (pixel in pixels) {
            buffer.putFloat(((pixel shr 16 and 0xFF) / 255.0f).toFloat())
            buffer.putFloat(((pixel shr 8 and 0xFF) / 255.0f).toFloat())
            buffer.putFloat(((pixel and 0xFF) / 255.0f).toFloat())
        }
        
        buffer.rewind()
        return buffer
    }

    private fun parseDetections(
        output: Array<FloatArray>,
        originalWidth: Int,
        originalHeight: Int
    ): List<DetectionResult> {
        val detections = mutableListOf<DetectionResult>()
        
        val transposed = Array(outputElementSize) { i ->
            FloatArray(numOutputElements) { j -> output[j][i] }
        }
        
        val scaleX = originalWidth.toFloat() / inputSize
        val scaleY = originalHeight.toFloat() / inputSize
        
        for (i in 0 until numOutputElements) {
            val cx = transposed[0][i]
            val cy = transposed[1][i]
            val w = transposed[2][i]
            val h = transposed[3][i]
            
            var maxScore = 0f
            var maxClassIdx = 0
            
            for (j in 0 until numClasses) {
                val score = transposed[4 + j][i]
                if (score > maxScore) {
                    maxScore = score
                    maxClassIdx = j
                }
            }
            
            if (maxScore > confidenceThreshold) {
                val x1 = (cx - w / 2) * scaleX
                val y1 = (cy - h / 2) * scaleY
                val x2 = (cx + w / 2) * scaleX
                val y2 = (cy + h / 2) * scaleY
                
                detections.add(
                    DetectionResult(
                        className = classNames[maxClassIdx],
                        confidence = maxScore,
                        boundingBox = RectF(x1, y1, x2, y2),
                        classId = maxClassIdx
                    )
                )
            }
        }
        
        return nms(detections)
    }

    private fun nms(detections: List<DetectionResult>, iouThreshold: Float = 0.45f): List<DetectionResult> {
        if (detections.isEmpty()) return emptyList()
        
        val sorted = detections.sortedByDescending { it.confidence }
        val result = mutableListOf<DetectionResult>()
        val suppressed = mutableSetOf<Int>()
        
        for (i in sorted.indices) {
            if (i in suppressed) continue
            result.add(sorted[i])
            
            for (j in i + 1 until sorted.size) {
                if (j in suppressed) continue
                if (iou(sorted[i].boundingBox, sorted[j].boundingBox) > iouThreshold) {
                    suppressed.add(j)
                }
            }
        }
        
        return result
    }

    private fun iou(box1: RectF, box2: RectF): Float {
        val x1 = maxOf(box1.left, box2.left)
        val y1 = maxOf(box1.top, box2.top)
        val x2 = minOf(box1.right, box2.right)
        val y2 = minOf(box1.bottom, box2.bottom)
        
        val intersection = maxOf(0f, x2 - x1) * maxOf(0f, y2 - y1)
        val area1 = box1.width() * box1.height()
        val area2 = box2.width() * box2.height()
        val union = area1 + area2 - intersection
        
        return if (union > 0) intersection / union else 0f
    }

    fun getInsectInfo(className: String): InsectInfo {
        return InsectDatabase.getInfo(className)
    }

    fun close() {
        interpreter?.close()
    }
}

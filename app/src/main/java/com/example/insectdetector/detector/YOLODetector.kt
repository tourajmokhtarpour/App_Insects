package com.example.insectdetector.detector

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.util.Log
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
    
    private var numDetections = 300
    private var numOutputValues = 6
    
    companion object {
        private const val TAG = "YOLODetector"
    }
    
    init {
        loadModel()
    }
    
    private fun loadModel() {
        try {
            Log.d(TAG, "شروع بارگذاری مدل...")
            
            val modelFile = File(context.cacheDir, "best_float16.tflite")
            
            if (!modelFile.exists()) {
                Log.d(TAG, "کپی فایل از assets به cache...")
                context.assets.open("best_float16.tflite").use { input ->
                    FileOutputStream(modelFile).use { output ->
                        input.copyTo(output)
                    }
                }
            }
            
            Log.d(TAG, "اندازه مدل: ${modelFile.length()} bytes")
            
            val fileInputStream = FileInputStream(modelFile)
            val channel = fileInputStream.channel
            val modelBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size())
            fileInputStream.close()
            
            val options = Interpreter.Options().apply {
                setNumThreads(4)
            }
            
            interpreter = Interpreter(modelBuffer, options)
            
            // تشخیص shape خروجی
            val outputTensor = interpreter!!.getOutputTensor(0)
            val shape = outputTensor.shape()
            Log.d(TAG, "Shape خروجی: ${shape.contentToString()}")
            
            if (shape.size == 3) {
                numDetections = shape[1]
                numOutputValues = shape[2]
            }
            
            Log.d(TAG, "مدل با موفقیت بارگذاری شد")
            Log.d(TAG, "تعداد تشخیص‌ها: $numDetections")
            Log.d(TAG, "تعداد مقادیر: $numOutputValues")
            
        } catch (e: Exception) {
            Log.e(TAG, "خطا در بارگذاری مدل: ${e.message}", e)
            throw e
        }
    }
    
    fun detect(bitmap: Bitmap): List<DetectionResult> {
        if (interpreter == null) {
            Log.e(TAG, "Interpreter null است!")
            return emptyList()
        }
        
        try {
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)
            val inputBuffer = bitmapToByteBuffer(resizedBitmap)
            
            val outputBuffer = Array(1) {
                Array(numDetections) { FloatArray(numOutputValues) }
            }
            
            interpreter!!.run(inputBuffer, outputBuffer)
            
            return parseDetections(outputBuffer[0], bitmap.width, bitmap.height)
            
        } catch (e: Exception) {
            Log.e(TAG, "خطا در تشخیص: ${e.message}", e)
            return emptyList()
        }
    }
    
    private fun bitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val buffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3)
        buffer.order(ByteOrder.nativeOrder())
        
        val pixels = IntArray(inputSize * inputSize)
        bitmap.getPixels(pixels, 0, inputSize, 0, 0, inputSize, inputSize)
        
        for (pixel in pixels) {
            buffer.putFloat(((pixel shr 16 and 0xFF) / 255.0f))
            buffer.putFloat(((pixel shr 8 and 0xFF) / 255.0f))
            buffer.putFloat(((pixel and 0xFF) / 255.0f))
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
        
        val scaleX = originalWidth.toFloat() / inputSize
        val scaleY = originalHeight.toFloat() / inputSize
        
        for (i in 0 until numDetections) {
            val values = output[i]
            
            // فرمت [1, 300, 6]: [x, y, w, h, confidence, class_id]
            if (numOutputValues == 6) {
                val cx = values[0]
                val cy = values[1]
                val w = values[2]
                val h = values[3]
                val confidence = values[4]
                val classId = values[5].toInt()
                
                if (confidence > confidenceThreshold && classId in classNames.indices) {
                    val x1 = (cx - w / 2) * scaleX
                    val y1 = (cy - h / 2) * scaleY
                    val x2 = (cx + w / 2) * scaleX
                    val y2 = (cy + h / 2) * scaleY
                    
                    detections.add(
                        DetectionResult(
                            className = classNames[classId],
                            confidence = confidence,
                            boundingBox = RectF(x1, y1, x2, y2),
                            classId = classId
                        )
                    )
                }
            }
            // فرمت [1, 8400, 73]: YOLO خام
            else if (numOutputValues == 4 + classNames.size) {
                val cx = values[0]
                val cy = values[1]
                val w = values[2]
                val h = values[3]
                
                var maxScore = 0f
                var maxClassIdx = 0
                
                for (j in 0 until classNames.size) {
                    val score = values[4 + j]
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
        }
        
        return detections.sortedByDescending { it.confidence }
    }
    
    fun getInsectInfo(className: String): InsectInfo {
        return InsectDatabase.getInfo(className)
    }
    
    fun close() {
        try {
            interpreter?.close()
            Log.d(TAG, "Interpreter بسته شد")
        } catch (e: Exception) {
            Log.e(TAG, "خطا در بستن: ${e.message}", e)
        }
    }
}

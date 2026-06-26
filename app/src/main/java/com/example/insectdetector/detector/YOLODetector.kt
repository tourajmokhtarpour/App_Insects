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
    
    // ✅ متغیرهای پویا بر اساس shape واقعی مدل
    private var numDetections = 300      // تعداد تشخیص‌ها
    private var numOutputValues = 6      // تعداد مقادیر هر تشخیص
    private var modelType = ModelType.YOLO_WITH_NMS  // نوع مدل

    enum class ModelType {
        YOLO_WITH_NMS,      // خروجی [1, 300, 6] - با NMS داخلی
        YOLO_RAW            // خروجی [1, 8400, 4+nc] - خام
    }

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
            Log.d(TAG, "مسیر cache: ${modelFile.absolutePath}")
            
            if (!modelFile.exists()) {
                Log.d(TAG, "فایل در cache وجود ندارد، کپی از assets...")
                context.assets.open("best_float16.tflite").use { input ->
                    FileOutputStream(modelFile).use { output ->
                        input.copyTo(output)
                    }
                }
                Log.d(TAG, "فایل با موفقیت کپی شد. اندازه: ${modelFile.length()} bytes")
            } else {
                Log.d(TAG, "فایل در cache وجود دارد. اندازه: ${modelFile.length()} bytes")
            }
            
            val modelBuffer = loadModelFile(modelFile)
            Log.d(TAG, "مدل بارگذاری شد، در حال ساخت Interpreter...")
            
            val options = Interpreter.Options().apply {
                setNumThreads(4)
            }
            
            interpreter = Interpreter(modelBuffer, options)
            
            // ✅ تشخیص خودکار نوع مدل از روی shape خروجی
            detectModelType()
            
            Log.d(TAG, "✅ مدل TFLite با موفقیت بارگذاری شد - نوع: $modelType")
        } catch (e: Exception) {
            Log.e(TAG, "❌ خطا در بارگذاری مدل: ${e.message}", e)
            throw e
        }
    }

    private fun detectModelType() {
        try {
            val outputTensor = interpreter?.getOutputTensor(0)
            val shape = outputTensor?.shape()
            
            Log.d(TAG, "Shape خروجی مدل: ${shape?.contentToString()}")
            
            if (shape != null && shape.size == 3) {
                numDetections = shape[1]
                numOutputValues = shape[2]
                
                if (numOutputValues == 6) {
                    // خروجی [1, 300, 6] = YOLO با NMS داخلی
                    modelType = ModelType.YOLO_WITH_NMS
                    Log.d(TAG, "✅ مدل شناسایی شد: YOLO با NMS داخلی")
                    Log.d(TAG, "   تعداد تشخیص‌ها: $numDetections")
                    Log.d(TAG, "   مقادیر هر تشخیص: $numOutputValues (x,y,w,h,conf,class_id)")
                } else {
                    // خروجی [1, 8400, 4+nc] = YOLO خام
                    modelType = ModelType.YOLO_RAW
                    Log.d(TAG, "✅ مدل شناسایی شد: YOLO خام")
                    Log.d(TAG, "   تعداد تشخیص‌ها: $numDetections")
                    Log.d(TAG, "   مقادیر هر تشخیص: $numOutputValues")
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "خطا در تشخیص نوع مدل، از حالت پیش‌فرض استفاده می‌شود: ${e.message}")
            modelType = ModelType.YOLO_WITH_NMS
            numDetections = 300
            numOutputValues = 6
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
        if (interpreter == null) {
            Log.e(TAG, "Interpreter null است!")
            return emptyList()
        }

        try {
            Log.d(TAG, "شروع تشخیص تصویر: ${bitmap.width}x${bitmap.height}")
            
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)
            val inputBuffer = bitmapToByteBuffer(resizedBitmap)
            
            // ✅ ساخت outputBuffer بر اساس shape واقعی مدل
            val outputBuffer = Array(1) { 
                Array(numDetections) { FloatArray(numOutputValues) } 
            }
            
            Log.d(TAG, "در حال اجرا کردن مدل... (shape: [1, $numDetections, $numOutputValues])")
            interpreter?.run(inputBuffer, outputBuffer)
            Log.d(TAG, "مدل اجرا شد، در حال پردازش نتایج...")
            
            // ✅ استفاده از parser مناسب بر اساس نوع مدل
            val results = when (modelType) {
                ModelType.YOLO_WITH_NMS -> parseDetectionsWithNMS(outputBuffer[0], bitmap.width, bitmap.height)
                ModelType.YOLO_RAW -> parseDetectionsRaw(outputBuffer[0], bitmap.width, bitmap.height)
            }
            
            Log.d(TAG, "تعداد تشخیص‌ها: ${results.size}")
            
            // نمایش 5 تشخیص برتر
            results.take(5).forEachIndexed { index, result ->
                Log.d(TAG, "  #${index + 1}: ${result.className} (${String.format("%.2f", result.confidence)})")
            }
            
            return results
        } catch (e: Exception) {
            Log.e(TAG, "خطا در detect: ${e.message}", e)
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

    // ✅ Parser برای مدل با NMS داخلی (خروجی [1, 300, 6])
    private fun parseDetectionsWithNMS(
        output: Array<FloatArray>,
        originalWidth: Int,
        originalHeight: Int
    ): List<DetectionResult> {
        val detections = mutableListOf<DetectionResult>()
        
        // ✅ مقیاس برای تبدیل مختصات از 224 به اندازه اصلی تصویر
        val scaleX = originalWidth.toFloat() / inputSize
        val scaleY = originalHeight.toFloat() / inputSize
        
        for (i in 0 until numDetections) {
            val values = output[i]
            
            // ✅ استخراج مقادیر: [x_center, y_center, width, height, confidence, class_id]
            val cx = values[0]
            val cy = values[1]
            val w = values[2]
            val h = values[3]
            val confidence = values[4]
            val classId = values[5].toInt()
            
            // فیلتر بر اساس confidence
            if (confidence > confidenceThreshold) {
                // بررسی معتبر بودن class_id
                if (classId < 0 || classId >= classNames.size) {
                    Log.w(TAG, "class_id نامعتبر: $classId (اندازه لیست: ${classNames.size})")
                    continue
                }
                
                // تبدیل مختصات از [0, 224] به مختصات تصویر اصلی
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
        
        // مرتب‌سازی بر اساس confidence (نزولی)
        return detections.sortedByDescending { it.confidence }
    }

    // ✅ Parser برای مدل خام (خروجی [1, 8400, 4+nc])
    private fun parseDetectionsRaw(
        output: Array<FloatArray>,
        originalWidth: Int,
        originalHeight: Int
    ): List<DetectionResult> {
        val detections = mutableListOf<DetectionResult>()
        val numClasses = classNames.size
        
        // Transpose: از [8400, 4+nc] به [4+nc, 8400]
        val transposed = Array(numOutputValues) { i ->
            FloatArray(numDetections) { j -> output[j][i] }
        }
        
        val scaleX = originalWidth.toFloat() / inputSize
        val scaleY = originalHeight.toFloat() / inputSize
        
        for (i in 0 until numDetections) {
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
        val area1 =

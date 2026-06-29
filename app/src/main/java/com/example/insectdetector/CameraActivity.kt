package com.example.insectdetector

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.insectdetector.databinding.ActivityCameraBinding
import com.example.insectdetector.detector.DetectionResult
import com.example.insectdetector.detector.YOLODetector
import com.example.insectdetector.utils.DataRecorder
import com.example.insectdetector.utils.LocationHelper
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

class CameraActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityCameraBinding
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var analysisExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    private var detector: YOLODetector? = null
    private lateinit var locationHelper: LocationHelper
    private lateinit var dataRecorder: DataRecorder
    private lateinit var prefs: SharedPreferences
    private var currentLocation: Location? = null
    private var currentPhotoFile: File? = null
    
    // ✅ متغیرهای تشخیص بلادرنگ
    private val isProcessing = AtomicBoolean(false)
    private var lastDetectionResults: List<DetectionResult> = emptyList()
    private var lastDetectionTime: Long = 0
    private val DETECTION_INTERVAL_MS = 500L // هر 500ms یکبار تشخیص
    
    // ✅ آخرین تشخیص معتبر برای ثبت
    private var lastValidDetection: DetectionResult? = null
    
    companion object {
        private const val TAG = "CameraActivity"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        locationHelper = LocationHelper(this)
        dataRecorder = DataRecorder(this)
        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        
        try {
            detector = YOLODetector(this)
        } catch (e: Exception) {
            Log.e(TAG, "خطا در بارگذاری مدل: ${e.message}", e)
            Toast.makeText(this, "خطا: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        
        cameraExecutor = Executors.newSingleThreadExecutor()
        analysisExecutor = Executors.newSingleThreadExecutor()
        
        getLocation()
        
        setupButtons()
        
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }
    
    private fun setupButtons() {
        // ✅ دکمه OK: فقط زمانی فعال است که تشخیص معتبر داریم
        binding.btnCapture.isEnabled = false
        binding.btnCapture.setOnClickListener { captureAndSave() }
        
        binding.btnCancel.setOnClickListener { finish() }
        
        // دکمه راهنما
        binding.btnInfo.setOnClickListener {
            Toast.makeText(
                this,
                "دوربین را به سمت حشره بگیرید.\nپس از تشخیص، دکمه ✓ را بزنید.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    
    private fun getLocation() {
        binding.tvLocation.isVisible = true
        binding.tvLocation.text = "📍 در حال دریافت موقعیت..."
        
        locationHelper.getCurrentLocation { location: Location? ->
            currentLocation = location
            runOnUiThread {
                if (location != null) {
                    binding.tvLocation.text = locationHelper.formatLocation(location)
                    Log.d(TAG, "موقعیت: ${location.latitude}, ${location.longitude}")
                } else {
                    binding.tvLocation.text = "📍 موقعیت مکانی: نامشخص"
                    Log.w(TAG, "موقعیت دریافت نشد")
                }
            }
        }
    }
    
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                
                // ✅ Preview
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(binding.previewView.surfaceProvider)
                    }
                
                // ✅ ImageCapture برای گرفتن عکس
                imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .build()
                
                // ✅ ImageAnalysis برای تشخیص بلادرنگ
                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                    .build()
                    .also {
                        it.setAnalyzer(analysisExecutor, ::analyzeImage)
                    }
                
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture,
                    imageAnalysis
                )
                
            } catch (exc: Exception) {
                Log.e(TAG, "خطا در راه‌اندازی دوربین: ${exc.message}", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }
    
    /**
     * ✅ تحلیل بلادرنگ فریم‌های دوربین
     */
    private fun analyzeImage(imageProxy: ImageProxy) {
        val currentTime = System.currentTimeMillis()
        
        // Throttle: فقط هر DETECTION_INTERVAL_MS یکبار تشخیص انجام شود
        if (currentTime - lastDetectionTime < DETECTION_INTERVAL_MS) {
            imageProxy.close()
            return
        }
        
        // جلوگیری از پردازش همزمان
        if (!isProcessing.compareAndSet(false, true)) {
            imageProxy.close()
            return
        }
        
        try {
            // تبدیل ImageProxy به Bitmap
            val bitmap = imageProxy.toBitmap()
            
            // چرخش bitmap اگر نیاز است
            val rotatedBitmap = rotateBitmapIfNeeded(bitmap, imageProxy.imageInfo.rotationDegrees)
            
            // تنظیم ابعاد تصویر در OverlayView
            runOnUiThread {
                binding.overlayView.setImageDimensions(rotatedBitmap.width, rotatedBitmap.height)
                binding.overlayView.setCameraMode(true)
            }
            
            // اجرای تشخیص
            val results = detector?.detect(rotatedBitmap) ?: emptyList()
            lastDetectionTime = currentTime
            
            // آزادسازی bitmap اگر کپی شده
            if (rotatedBitmap != bitmap) {
                rotatedBitmap.recycle()
            }
            
            // به‌روزرسانی UI در Main Thread
            runOnUiThread {
                updateDetectionUI(results)
            }
            
            // ذخیره نتایج برای ثبت بعدی
            lastDetectionResults = results
            
            // به‌روزرسانی آخرین تشخیص معتبر
            if (results.isNotEmpty() && results[0].confidence >= 0.50f) {
                lastValidDetection = results[0]
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "خطا در تحلیل تصویر: ${e.message}", e)
        } finally {
            isProcessing.set(false)
            imageProxy.close()
        }
    }
    
    /**
     * چرخش Bitmap بر اساس rotation
     */
    private fun rotateBitmapIfNeeded(bitmap: Bitmap, rotationDegrees: Int): Bitmap {
        if (rotationDegrees == 0) return bitmap
        
        val matrix = Matrix()
        matrix.postRotate(rotationDegrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
    
    /**
     * ✅ به‌روزرسانی UI با نتایج تشخیص
     */
    private fun updateDetectionUI(results: List<DetectionResult>) {
        if (results.isEmpty()) {
            // هیچ تشخیصی نیست
            binding.overlayView.clearDetections()
            binding.cardDetectionInfo.isVisible = false
            binding.btnCapture.isEnabled = false
            return
        }
        
        val topResult = results[0]
        
        // رسم Bounding Box
        binding.overlayView.setDetections(results)
        
        if (topResult.confidence >= 0.50f) {
            // ✅ تشخیص معتبر - نمایش اطلاعات و فعال کردن دکمه OK
            binding.cardDetectionInfo.isVisible = true
            binding.tvDetectedClass.text = "✅ ${topResult.className}"
            binding.tvDetectedConfidence.text = "اطمینان: ${String.format("%.1f", topResult.confidence * 100)}٪"
            binding.tvDetectedConfidence.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
            binding.btnCapture.isEnabled = true
            
            // تغییر رنگ دکمه OK به سبز
            binding.btnCapture.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.holo_green_light)
            
        } else {
            // تشخیص با اطمینان پایین
            binding.cardDetectionInfo.isVisible = true
            binding.tvDetectedClass.text = "⚠️ ${topResult.className}"
            binding.tvDetectedConfidence.text = "اطمینان: ${String.format("%.1f", topResult.confidence * 100)}٪ (کمتر از 50٪)"
            binding.tvDetectedConfidence.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_light))
            binding.btnCapture.isEnabled = false
            binding.btnCapture.backgroundTintList = null
        }
    }
    
    /**
     * ✅ گرفتن عکس و ثبت داده‌ها
     */
    private fun captureAndSave() {
        val detection = lastValidDetection
        if (detection == null) {
            Toast.makeText(this, "❌ تشخیص معتبری وجود ندارد", Toast.LENGTH_SHORT).show()
            return
        }
        
        val imageCapture = imageCapture ?: return
        
        // غیرفعال کردن دکمه‌ها حین پردازش
        binding.btnCapture.isEnabled = false
        binding.progressBar.isVisible = true
        
        val photoFile = File(
            externalCacheDir,
            SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(Date()) + ".jpg"
        )
        currentPhotoFile = photoFile
        
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                    if (bitmap != null) {
                        saveDetectionData(bitmap, detection, photoFile)
                    } else {
                        Log.e(TAG, "خطا در بارگذاری bitmap")
                        binding.progressBar.isVisible = false
                        binding.btnCapture.isEnabled = true
                    }
                }
                
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "خطا در عکس‌برداری", exc)
                    Toast.makeText(this@CameraActivity, "خطا در عکس‌برداری", Toast.LENGTH_SHORT).show()
                    binding.progressBar.isVisible = false
                    binding.btnCapture.isEnabled = true
                }
            }
        )
    }
    
    /**
     * ✅ ثبت داده‌ها و انتقال به ResultActivity
     */
    private fun saveDetectionData(bitmap: Bitmap, detection: DetectionResult, photoFile: File) {
        cameraExecutor.execute {
            try {
                val userName = prefs.getString("user_name", "نامشخص") ?: "نامشخص"
                val latitude = currentLocation?.latitude ?: 0.0
                val longitude = currentLocation?.longitude ?: 0.0
                
                val recordResult = dataRecorder.recordDetection(
                    bitmap = bitmap,
                    className = detection.className,
                    confidence = detection.confidence,
                    latitude = latitude,
                    longitude = longitude,
                    userName = userName
                )
                
                runOnUiThread {
                    binding.progressBar.isVisible = false
                    
                    if (recordResult.success) {
                        Toast.makeText(
                            this,
                            "✅ ثبت شد: ${detection.className}\nکلید: ${recordResult.primaryKey}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "❌ خطا: ${recordResult.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    
                    // انتقال به ResultActivity برای نمایش نتایج کامل
                    val intent = Intent(this, ResultActivity::class.java).apply {
                        putExtra("image_uri", Uri.fromFile(photoFile).toString())
                        putExtra("class_name", detection.className)
                        putExtra("confidence", detection.confidence)
                        putExtra("class_id", detection.classId)
                        putExtra("latitude", latitude)
                        putExtra("longitude", longitude)
                        putExtra("primary_key", recordResult.primaryKey)
                        putExtra("from_camera", true)
                    }
                    startActivity(intent)
                    finish()
                }
            } catch (e: Exception) {
                Log.e(TAG, "خطا در ثبت داده‌ها: ${e.message}", e)
                runOnUiThread {
                    binding.progressBar.isVisible = false
                    binding.btnCapture.isEnabled = true
                    Toast.makeText(this, "خطا: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
    
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "دسترسی‌ها اعطا نشدند", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        analysisExecutor.shutdown()
        detector?.close()
    }
}

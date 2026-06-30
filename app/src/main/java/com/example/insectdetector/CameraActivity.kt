package com.example.insectdetector

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.YuvImage
import android.location.Location
import android.net.Uri
import android.os.Bundle
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
import com.example.insectdetector.utils.ServerUploader
import java.io.ByteArrayOutputStream
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
    
    // ✅ جدید: آپلود به سرور
    private lateinit var serverUploader: ServerUploader
    
    // ✅ متغیرهای تشخیص بلادرنگ
    private val isProcessing = AtomicBoolean(false)
    private var frameCount = 0
    private var lastDetectionTime: Long = 0
    private val DETECTION_INTERVAL_MS = 500L
    
    // ✅ آخرین تشخیص معتبر
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
        
        Log.d(TAG, "🚀 CameraActivity onCreate شروع شد")
        
        locationHelper = LocationHelper(this)
        dataRecorder = DataRecorder(this)
        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        
        // ✅ مقداردهی ServerUploader
        serverUploader = ServerUploader(this)
        
        try {
            detector = YOLODetector(this)
            Log.d(TAG, "✅ مدل با موفقیت بارگذاری شد")
        } catch (e: Exception) {
            Log.e(TAG, "❌ خطا در بارگذاری مدل: ${e.message}", e)
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
        binding.btnCapture.isEnabled = false
        binding.btnCapture.setOnClickListener { captureAndSave() }
        binding.btnCancel.setOnClickListener { finish() }
        
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
                    Log.d(TAG, "📍 موقعیت: ${location.latitude}, ${location.longitude}")
                } else {
                    binding.tvLocation.text = "📍 موقعیت مکانی: نامشخص"
                }
            }
        }
    }
    
    private fun startCamera() {
        Log.d(TAG, "📷 شروع راه‌اندازی دوربین...")
        
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(binding.previewView.surfaceProvider)
                    }
                
                imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .build()
                
                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        Log.d(TAG, "🔍 تنظیم ImageAnalysis Analyzer...")
                        it.setAnalyzer(analysisExecutor, ImageAnalysis.Analyzer { imageProxy ->
                            analyzeImage(imageProxy)
                        })
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
                
                Log.d(TAG, "✅ دوربین با موفقیت راه‌اندازی شد - ImageAnalysis فعال است")
                
            } catch (exc: Exception) {
                Log.e(TAG, "❌ خطا در راه‌اندازی دوربین: ${exc.message}", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }
    
    private fun analyzeImage(imageProxy: ImageProxy) {
        frameCount++
        
        if (frameCount % 10 == 0) {
            Log.d(TAG, "📸 فریم #$frameCount دریافت شد - اندازه: ${imageProxy.width}x${imageProxy.height}")
        }
        
        val currentTime = System.currentTimeMillis()
        
        if (currentTime - lastDetectionTime < DETECTION_INTERVAL_MS) {
            imageProxy.close()
            return
        }
        
        if (!isProcessing.compareAndSet(false, true)) {
            if (frameCount % 50 == 0) {
                Log.d(TAG, "⏳ پردازش قبلی هنوز در حال انجام است - فریم رد شد")
            }
            imageProxy.close()
            return
        }
        
        try {
            Log.d(TAG, "🔄 شروع پردازش فریم #$frameCount...")
            
            val bitmap = imageProxyToBitmapSimple(imageProxy)
            
            if (bitmap == null) {
                Log.e(TAG, "❌ تبدیل به Bitmap ناموفق بود - فریم #$frameCount")
                imageProxy.close()
                isProcessing.set(false)
                return
            }
            
            Log.d(TAG, "✅ Bitmap ساخته شد: ${bitmap.width}x${bitmap.height}")
            
            runOnUiThread {
                binding.overlayView.setImageDimensions(bitmap.width, bitmap.height)
                binding.overlayView.setCameraMode(true)
            }
            
            val results = detector?.detect(bitmap) ?: emptyList()
            lastDetectionTime = currentTime
            
            Log.d(TAG, "🎯 تشخیص انجام شد: ${results.size} نتیجه")
            
            if (results.isNotEmpty() && results[0].confidence >= 0.50f) {
                lastValidDetection = results[0]
                Log.d(TAG, "✅ تشخیص معتبر: ${results[0].className} (${results[0].confidence})")
            }
            
            runOnUiThread {
                updateDetectionUI(results)
            }
            
            bitmap.recycle()
            
        } catch (e: Exception) {
            Log.e(TAG, "❌ خطا در تحلیل تصویر: ${e.message}", e)
            e.printStackTrace()
        } finally {
            isProcessing.set(false)
            imageProxy.close()
        }
    }
    
    private fun imageProxyToBitmapSimple(imageProxy: ImageProxy): Bitmap? {
        return try {
            val yBuffer = imageProxy.planes[0].buffer
            val uBuffer = imageProxy.planes[1].buffer
            val vBuffer = imageProxy.planes[2].buffer
            
            val ySize = yBuffer.remaining()
            val uSize = uBuffer.remaining()
            val vSize = vBuffer.remaining()
            
            val nv21 = ByteArray(ySize + uSize + vSize)
            
            yBuffer.get(nv21, 0, ySize)
            vBuffer.get(nv21, ySize, vSize)
            uBuffer.get(nv21, ySize + vSize, uSize)
            
            val yuvImage = YuvImage(
                nv21,
                ImageFormat.NV21,
                imageProxy.width,
                imageProxy.height,
                null
            )
            
            val out = ByteArrayOutputStream()
            yuvImage.compressToJpeg(
                android.graphics.Rect(0, 0, imageProxy.width, imageProxy.height),
                80,
                out
            )
            
            val imageBytes = out.toByteArray()
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            
            if (imageProxy.imageInfo.rotationDegrees != 0) {
                val matrix = Matrix()
                matrix.postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
                val rotated = Bitmap.createBitmap(
                    bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
                )
                if (rotated != bitmap) {
                    bitmap.recycle()
                }
                rotated
            } else {
                bitmap
            }
        } catch (e: Exception) {
            Log.e(TAG, "خطا در imageProxyToBitmapSimple: ${e.message}", e)
            null
        }
    }
    
    private fun updateDetectionUI(results: List<DetectionResult>) {
        if (results.isEmpty()) {
            binding.overlayView.clearDetections()
            binding.cardDetectionInfo.isVisible = false
            binding.btnCapture.isEnabled = false
            return
        }
        
        val topResult = results[0]
        
        binding.overlayView.setDetections(results)
        
        if (topResult.confidence >= 0.50f) {
            binding.cardDetectionInfo.isVisible = true
            binding.tvDetectedClass.text = "✅ ${topResult.className}"
            binding.tvDetectedConfidence.text = "اطمینان: ${String.format("%.1f", topResult.confidence * 100)}٪"
            binding.tvDetectedConfidence.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
            binding.btnCapture.isEnabled = true
            binding.btnCapture.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.holo_green_light)
        } else {
            binding.cardDetectionInfo.isVisible = true
            binding.tvDetectedClass.text = "⚠️ ${topResult.className}"
            binding.tvDetectedConfidence.text = "اطمینان: ${String.format("%.1f", topResult.confidence * 100)}٪ (کمتر از 50٪)"
            binding.tvDetectedConfidence.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_light))
            binding.btnCapture.isEnabled = false
            binding.btnCapture.backgroundTintList = null
        }
    }
    
    private fun captureAndSave() {
        val detection = lastValidDetection
        if (detection == null) {
            Toast.makeText(this, "❌ تشخیص معتبری وجود ندارد", Toast.LENGTH_SHORT).show()
            return
        }
        
        val imageCapture = imageCapture ?: return
        
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
                        binding.progressBar.isVisible = false
                        binding.btnCapture.isEnabled = true
                    }
                }
                
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "خطا در عکس‌برداری", exc)
                    binding.progressBar.isVisible = false
                    binding.btnCapture.isEnabled = true
                }
            }
        )
    }
    
    private fun saveDetectionData(bitmap: Bitmap, detection: DetectionResult, photoFile: File) {
        cameraExecutor.execute {
            try {
                val userName = prefs.getString("user_name", "نامشخص") ?: "نامشخص"
                val latitude = currentLocation?.latitude ?: 0.0
                val longitude = currentLocation?.longitude ?: 0.0
                
                // ✅ ثبت محلی
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
                    }
                    
                    // ✅ آپلود به سرور (جدید)
                    uploadToServer(photoFile, detection, latitude, longitude, userName, recordResult.primaryKey)
                    
                    // انتقال به ResultActivity
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
                Log.e(TAG, "خطا در ثبت: ${e.message}", e)
                runOnUiThread {
                    binding.progressBar.isVisible = false
                    binding.btnCapture.isEnabled = true
                }
            }
        }
    }
    
    /**
     * ✅ تابع جدید: آپلود به سرور
     */
    private fun uploadToServer(
        photoFile: File,
        detection: DetectionResult,
        latitude: Double,
        longitude: Double,
        userName: String,
        primaryKey: String
    ) {
        if (!isNetworkAvailable()) {
            Log.w(TAG, "⚠️ اینترنت نیست - ذخیره برای آپلود بعدی")
            savePendingUpload(photoFile, detection, latitude, longitude, userName, primaryKey)
            return
        }
        
        Log.d(TAG, "📤 در حال آپلود به سرور...")
        
        serverUploader.uploadDetection(
            imageFile = photoFile,
            primaryKey = primaryKey,
            className = detection.className,
            confidence = detection.confidence,
            latitude = latitude,
            longitude = longitude,
            userName = userName
        ) { result ->
            runOnUiThread {
                if (result.success) {
                    Log.d(TAG, "✅ آپلود موفق به سرور")
                    Toast.makeText(
                        this,
                        "📤 داده به سرور ارسال شد",
                        Toast.LENGTH_SHORT
                    ).show()
                    
                    clearPendingUpload(primaryKey)
                } else {
                    Log.e(TAG, "❌ آپلود ناموفق: ${result.message}")
                    Toast.makeText(
                        this,
                        "⚠️ خطا در آپلود: ${result.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    
                    savePendingUpload(photoFile, detection, latitude, longitude, userName, primaryKey)
                }
            }
        }
    }
    
    /**
     * ✅ بررسی اتصال اینترنت
     */
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(android.content.Context.CONNECTIVITY_SERVICE) 
            as? android.net.ConnectivityManager
        val network = connectivityManager?.activeNetwork
        val capabilities = connectivityManager?.getNetworkCapabilities(network)
        return capabilities?.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
    
    /**
     * ✅ ذخیره برای آپلود بعدی (وقتی اینترنت نیست)
     */
    private fun savePendingUpload(
        photoFile: File,
        detection: DetectionResult,
        latitude: Double,
        longitude: Double,
        userName: String,
        primaryKey: String
    ) {
        val pendingPrefs = getSharedPreferences("pending_uploads", MODE_PRIVATE)
        val editor = pendingPrefs.edit()
        
        editor.putString("pending_${primaryKey}_path", photoFile.absolutePath)
        editor.putString("pending_${primaryKey}_class", detection.className)
        editor.putFloat("pending_${primaryKey}_confidence", detection.confidence)
        editor.putFloat("pending_${primaryKey}_latitude", latitude.toFloat())
        editor.putFloat("pending_${primaryKey}_longitude", longitude.toFloat())
        editor.putString("pending_${primaryKey}_user", userName)
        editor.putBoolean("pending_${primaryKey}_ready", true)
        editor.apply()
        
        Log.d(TAG, "💾 ذخیره شد برای آپلود بعدی: $primaryKey")
    }
    
    /**
     * ✅ پاک کردن از pending
     */
    private fun clearPendingUpload(primaryKey: String) {
        val pendingPrefs = getSharedPreferences("pending_uploads", MODE_PRIVATE)
        val editor = pendingPrefs.edit()
        editor.remove("pending_${primaryKey}_path")
        editor.remove("pending_${primaryKey}_class")
        editor.remove("pending_${primaryKey}_confidence")
        editor.remove("pending_${primaryKey}_latitude")
        editor.remove("pending_${primaryKey}_longitude")
        editor.remove("pending_${primaryKey}_user")
        editor.remove("pending_${primaryKey}_ready")
        editor.apply()
    }
    
    /**
     * ✅ آپلود موارد pending (وقتی اینترنت برگشت)
     */
    private fun uploadPendingItems() {
        val pendingPrefs = getSharedPreferences("pending_uploads", MODE_PRIVATE)
        val allKeys = pendingPrefs.all.keys.filter { it.endsWith("_ready") }
        
        if (allKeys.isEmpty()) {
            Log.d(TAG, "📭 هیچ مورد pending نیست")
            return
        }
        
        Log.d(TAG, "📤 آپلود ${allKeys.size} مورد pending...")
        
        for (key in allKeys) {
            val primaryKey = key.removePrefix("pending_").removeSuffix("_ready")
            
            val path = pendingPrefs.getString("pending_${primaryKey}_path", null) ?: continue
            val className = pendingPrefs.getString("pending_${primaryKey}_class", "") ?: ""
            val confidence = pendingPrefs.getFloat("pending_${primaryKey}_confidence", 0f)
            val latitude = pendingPrefs.getFloat("pending_${primaryKey}_latitude", 0f).toDouble()
            val longitude = pendingPrefs.getFloat("pending_${primaryKey}_longitude", 0f).toDouble()
            val userName = pendingPrefs.getString("pending_${primaryKey}_user", "") ?: ""
            
            val file = File(path)
            if (!file.exists()) {
                clearPendingUpload(primaryKey)
                continue
            }
            
            serverUploader.uploadDetection(
                imageFile = file,
                primaryKey = primaryKey,
                className = className,
                confidence = confidence,
                latitude = latitude,
                longitude = longitude,
                userName = userName
            ) { result ->
                if (result.success) {
                    clearPendingUpload(primaryKey)
                    Log.d(TAG, "✅ آپلود pending موفق: $primaryKey")
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
                finish()
            }
        }
    }
    
    /**
     * ✅ بررسی و آپلود موارد pending وقتی اپ به foreground می‌آید
     */
    override fun onResume() {
        super.onResume()
        if (isNetworkAvailable()) {
            uploadPendingItems()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        analysisExecutor.shutdown()
        detector?.close()
    }
}

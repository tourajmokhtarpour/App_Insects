package com.example.insectdetector

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.insectdetector.databinding.ActivityCameraBinding
import com.example.insectdetector.detector.YOLODetector
import com.example.insectdetector.utils.DataRecorder
import com.example.insectdetector.utils.LocationHelper
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityCameraBinding
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    private var detector: YOLODetector? = null
    private lateinit var locationHelper: LocationHelper
    private lateinit var dataRecorder: DataRecorder
    private lateinit var prefs: SharedPreferences
    private var currentLocation: Location? = null
    
    // ✅ متغیر جدید برای ذخیره مسیر فایل عکس فعلی
    private var currentPhotoFile: File? = null
    
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
        
        getLocation()
        
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        
        binding.btnCapture.setOnClickListener { takePhoto() }
        binding.btnCancel.setOnClickListener { finish() }
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
                
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }
                
                imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .build()
                
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                
            } catch (exc: Exception) {
                Log.e(TAG, "خطا در دوربین: ${exc.message}", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }
    
    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        
        // ✅ ایجاد فایل عکس با نام یکتا
        val photoFile = File(
            externalCacheDir,
            SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(Date()) + ".jpg"
        )
        
        // ✅ ذخیره در متغیر کلاس برای استفاده بعدی
        currentPhotoFile = photoFile
        
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                    if (bitmap != null) {
                        detectInImage(bitmap, photoFile)
                    } else {
                        Log.e(TAG, "خطا در بارگذاری bitmap از فایل")
                        Toast.makeText(this@CameraActivity, "خطا در بارگذاری تصویر", Toast.LENGTH_SHORT).show()
                    }
                }
                
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "خطا در عکس‌برداری", exc)
                    Toast.makeText(this@CameraActivity, "خطا در عکس‌برداری", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
    
    // ✅ اضافه شدن پارامتر photoFile برای ارسال به ResultActivity
    private fun detectInImage(bitmap: android.graphics.Bitmap, photoFile: File) {
        binding.progressBar.isVisible = true
        
        cameraExecutor.execute {
            val results = detector?.detect(bitmap) ?: emptyList()
            
            runOnUiThread {
                binding.progressBar.isVisible = false
                
                if (results.isNotEmpty()) {
                    val topResult = results[0]
                    
                    if (topResult.confidence >= 0.50f) {
                        val userName = prefs.getString("user_name", "نامشخص") ?: "نامشخص"
                        val latitude = currentLocation?.latitude ?: 0.0
                        val longitude = currentLocation?.longitude ?: 0.0
                        
                        val recordResult = dataRecorder.recordDetection(
                            bitmap = bitmap,
                            className = topResult.className,
                            confidence = topResult.confidence,
                            latitude = latitude,
                            longitude = longitude,
                            userName = userName
                        )
                        
                        if (recordResult.success) {
                            Toast.makeText(
                                this,
                                "✅ ثبت شد: ${topResult.className}\nکلید: ${recordResult.primaryKey}",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                this,
                                "❌ خطا: ${recordResult.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        
                        // ✅ ارسال اطلاعات به ResultActivity
                        val intent = Intent(this, ResultActivity::class.java).apply {
                            // ✅ ارسال URI تصویر برای نمایش Bounding Box
                            putExtra("image_uri", Uri.fromFile(photoFile).toString())
                            putExtra("class_name", topResult.className)
                            putExtra("confidence", topResult.confidence)
                            putExtra("class_id", topResult.classId)
                            putExtra("latitude", latitude)
                            putExtra("longitude", longitude)
                            putExtra("primary_key", recordResult.primaryKey)
                            putExtra("from_camera", true)  // ✅ نشان می‌دهد از دوربین آمده
                        }
                        startActivity(intent)
                        finish()
                        
                    } else {
                        Toast.makeText(
                            this,
                            "درصد تشخیص پایین است: ${String.format("%.1f", topResult.confidence * 100)}٪\nحداقل 50٪ لازم است",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(this, "حشره‌ای تشخیص داده نشد", Toast.LENGTH_SHORT).show()
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
    
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        detector?.close()
    }
}

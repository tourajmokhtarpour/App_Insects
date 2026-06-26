package com.example.insectdetector

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
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
import com.example.insectdetector.detector.YOLODetector
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityCameraBinding
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    private var detector: YOLODetector? = null
    
    companion object {
        private const val TAG = "CameraActivity"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        try {
            detector = YOLODetector(this)
        } catch (e: Exception) {
            Log.e(TAG, "خطا در بارگذاری مدل: ${e.message}", e)
            Toast.makeText(this, "خطا: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        
        cameraExecutor = Executors.newSingleThreadExecutor()
        
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        
        binding.btnCapture.setOnClickListener { takePhoto() }
        binding.btnCancel.setOnClickListener { finish() }
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
        
        val photoFile = File(
            externalCacheDir,
            SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(Date()) + ".jpg"
        )
        
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                    if (bitmap != null) {
                        detectInImage(bitmap)
                    }
                }
                
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "خطا در عکس‌برداری", exc)
                }
            }
        )
    }
    
    private fun detectInImage(bitmap: android.graphics.Bitmap) {
        binding.progressBar.isVisible = true
        
        cameraExecutor.execute {
            val results = detector?.detect(bitmap) ?: emptyList()
            
            runOnUiThread {
                binding.progressBar.isVisible = false
                
                if (results.isNotEmpty()) {
                    val topResult = results[0]
                    val intent = Intent(this, ResultActivity::class.java).apply {
                        putExtra("class_name", topResult.className)
                        putExtra("confidence", topResult.confidence)
                        putExtra("class_id", topResult.classId)
                    }
                    startActivity(intent)
                    finish()
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

package com.example.insectdetector

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.insectdetector.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: SharedPreferences

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        
        if (fineLocationGranted || coarseLocationGranted) {
            checkAndEnableGPS()
        } else {
            Toast.makeText(this, "دسترسی به موقعیت مکانی لازم است", Toast.LENGTH_LONG).show()
        }
    }

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openCamera()
        } else {
            Toast.makeText(this, "دسترسی دوربین لازم است", Toast.LENGTH_SHORT).show()
        }
    }

    private val storagePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "دسترسی ذخیره‌سازی اعطا شد", Toast.LENGTH_SHORT).show()
        }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            checkAndEnableGPSForGallery(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        
        // بررسی نام کاربر
        val userName = prefs.getString("user_name", "")
        if (userName.isNullOrEmpty()) {
            askUserName()
        }

        setupClickListeners()
    }

    private fun askUserName() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("نام کاربر")
        builder.setMessage("لطفاً نام خود را وارد کنید:")
        
        val input = android.widget.EditText(this)
        input.hint = "نام و نام خانوادگی"
        builder.setView(input)
        
        builder.setPositiveButton("تأیید") { dialog, _ ->
            val name = input.text.toString().trim()
            if (name.isNotEmpty()) {
                prefs.edit().putString("user_name", name).apply()
                Toast.makeText(this, "نام ثبت شد: $name", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        
        builder.setNegativeButton("انصراف") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun setupClickListeners() {
        binding.btnTakePhoto.setOnClickListener {
            checkAllPermissionsForCamera()
        }

        binding.btnSelectFromGallery.setOnClickListener {
            checkAllPermissionsForGallery()
        }
    }

    private fun checkAllPermissionsForCamera() {
        val permissions = mutableListOf<String>()
        
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
            != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
            != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA)
        }
        
        if (permissions.isEmpty()) {
            checkAndEnableGPS()
        } else {
            locationPermissionLauncher.launch(permissions.toTypedArray())
        }
    }

    private fun checkAllPermissionsForGallery() {
        val permissions = mutableListOf<String>()
        
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
            != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        
        if (permissions.isEmpty()) {
            openGallery()
        } else {
            locationPermissionLauncher.launch(permissions.toTypedArray())
        }
    }

    private fun checkAndEnableGPS() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        
        if (!isGPSEnabled) {
            Toast.makeText(this, "لطفاً GPS را روشن کنید", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        } else {
            openCamera()
        }
    }

    private fun checkAndEnableGPSForGallery(uri: Uri) {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        
        if (!isGPSEnabled) {
            Toast.makeText(this, "لطفاً GPS را روشن کنید", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        } else {
            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("image_uri", uri.toString())
            }
            startActivity(intent)
        }
    }

    private fun openCamera() {
        try {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "خطا: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openGallery() {
        try {
            galleryLauncher.launch("image/*")
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "خطا: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}

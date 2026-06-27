package com.example.insectdetector.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationHelper(private val context: Context) {
    
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    
    companion object {
        private const val TAG = "LocationHelper"
    }
    
    fun getCurrentLocation(onLocationReceived: (Location?) -> Unit) {
        // بررسی permission
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(TAG, "Permission not granted")
            onLocationReceived(null)
            return
        }
        
        // دریافت آخرین موقعیت شناخته شده
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    Log.d(TAG, "موقعیت دریافت شد: ${location.latitude}, ${location.longitude}")
                    onLocationReceived(location)
                } else {
                    requestNewLocation(onLocationReceived)
                }
            }
            .addOnFailureListener { e: Exception ->
                Log.e(TAG, "خطا در دریافت موقعیت: ${e.message}", e)
                onLocationReceived(null)
            }
    }
    
    private fun requestNewLocation(onLocationReceived: (Location?) -> Unit) {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onLocationReceived(null)
            return
        }
        
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .setMinUpdateIntervalMillis(2000)
            .setMaxUpdateDelayMillis(10000)
            .build()
        
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                if (location != null) {
                    Log.d(TAG, "موقعیت جدید: ${location.latitude}, ${location.longitude}")
                    onLocationReceived(location)
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }
        }
        
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: Exception) {
            Log.e(TAG, "خطا در درخواست موقعیت: ${e.message}", e)
            onLocationReceived(null)
        }
        
        // Timeout بعد از 10 ثانیه
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }, 10000)
    }
    
    fun formatLocation(location: Location?): String {
        if (location == null) {
            return "موقعیت مکانی: نامشخص"
        }
        return String.format(
            "📍 موقعیت: %.6f, %.6f",
            location.latitude,
            location.longitude
        )
    }
    
    fun getGoogleMapsLink(location: Location?): String {
        if (location == null) {
            return ""
        }
        return "https://maps.google.com/?q=${location.latitude},${location.longitude}"
    }
}

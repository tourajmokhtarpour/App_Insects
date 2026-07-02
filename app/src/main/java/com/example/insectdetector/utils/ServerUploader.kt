package com.example.insectdetector.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ServerUploader(private val context: Context) {
    
    companion object {
        private const val TAG = "ServerUploader"
        
        // ✅ آدرس سرور Hugging Face
        const val SERVER_URL = "https://touraj732-insect-detector-server.hf.space"
        
        // ✅ تنظیمات بهینه‌سازی
        private const val MAX_WIDTH = 1280          // حداکثر عرض
        private const val MAX_HEIGHT = 720          // حداکثر ارتفاع
        private const val JPEG_QUALITY = 70         // کیفیت JPEG (0-100)
    }
    
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(180, java.util.concurrent.TimeUnit.SECONDS)    // افزایش به 3 دقیقه
        .writeTimeout(180, java.util.concurrent.TimeUnit.SECONDS)   // افزایش به 3 دقیقه
        .build()
    
    /**
     * ✅ بهینه‌سازی تصویر قبل از آپلود
     */
    private fun optimizeImage(imageFile: File): File {
        try {
            Log.d(TAG, "🔧 شروع بهینه‌سازی تصویر...")
            
            // بارگذاری bitmap
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeFile(imageFile.absolutePath, options)
            
            // محاسبه sample size
            val originalWidth = options.outWidth
            val originalHeight = options.outHeight
            var sampleSize = 1
            
            while (originalWidth / sampleSize > MAX_WIDTH || 
                   originalHeight / sampleSize > MAX_HEIGHT) {
                sampleSize *= 2
            }
            
            // بارگذاری bitmap با sample size
            val decodeOptions = BitmapFactory.Options().apply {
                inSampleSize = sampleSize
            }
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath, decodeOptions)
            
            if (bitmap == null) {
                Log.e(TAG, "❌ خطا در بارگذاری bitmap")
                return imageFile
            }
            
            // resize اگر لازم باشد
            val finalBitmap = if (bitmap.width > MAX_WIDTH || bitmap.height > MAX_HEIGHT) {
                val ratio = minOf(
                    MAX_WIDTH.toFloat() / bitmap.width,
                    MAX_HEIGHT.toFloat() / bitmap.height
                )
                val newWidth = (bitmap.width * ratio).toInt()
                val newHeight = (bitmap.height * ratio).toInt()
                Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
            } else {
                bitmap
            }
            
            // ذخیره در فایل موقت
            val optimizedFile = File(context.cacheDir, "optimized_${imageFile.name}")
            FileOutputStream(optimizedFile).use { out ->
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, out)
            }
            
            // آزادسازی حافظه
            if (finalBitmap != bitmap) {
                finalBitmap.recycle()
            }
            bitmap.recycle()
            
            val newSize = optimizedFile.length() / 1024
            Log.d(TAG, "✅ بهینه‌سازی موفق: ${imageFile.length() / 1024}KB → ${newSize}KB")
            
            return optimizedFile
            
        } catch (e: Exception) {
            Log.e(TAG, "❌ خطا در بهینه‌سازی: ${e.message}")
            return imageFile
        }
    }
    
    /**
     * آپلود تصویر و داده‌ها به سرور
     */
    fun uploadDetection(
        imageFile: File,
        primaryKey: String,
        className: String,
        confidence: Float,
        latitude: Double,
        longitude: Double,
        userName: String,
        callback: (UploadResult) -> Unit
    ) {
        Log.d(TAG, "═══════════════════════════════════════")
        Log.d(TAG, "📤 شروع آپلود: $primaryKey")
        Log.d(TAG, "📍 آدرس سرور: $SERVER_URL")
        Log.d(TAG, "📏 حجم فایل اصلی: ${imageFile.length() / 1024} KB")
        
        if (!imageFile.exists()) {
            Log.e(TAG, "❌ فایل تصویر وجود ندارد")
            callback(UploadResult(false, "فایل تصویر وجود ندارد"))
            return
        }
        
        if (!isNetworkAvailable()) {
            Log.e(TAG, "❌ اتصال اینترنت برقرار نیست")
            callback(UploadResult(false, "اتصال اینترنت برقرار نیست"))
            return
        }
        
        // ✅ بهینه‌سازی تصویر قبل از آپلود
        val optimizedFile = optimizeImage(imageFile)
        Log.d(TAG, "📏 حجم فایل بهینه: ${optimizedFile.length() / 1024} KB")
        
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", optimizedFile.name,
                optimizedFile.asRequestBody("image/jpeg".toMediaType()))
            .addFormDataPart("primary_key", primaryKey)
            .addFormDataPart("class_name", className)
            .addFormDataPart("confidence", confidence.toString())
            .addFormDataPart("latitude", latitude.toString())
            .addFormDataPart("longitude", longitude.toString())
            .addFormDataPart("user_name", userName)
            .build()
        
        val request = Request.Builder()
            .url("$SERVER_URL/api/upload")
            .post(requestBody)
            .build()
        
        Log.d(TAG, "🚀 ارسال درخواست به: ${request.url}")
        
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "═══════════════════════════════════════")
                Log.e(TAG, "❌ خطا در آپلود: ${e.message}")
                Log.e(TAG, "🔗 URL: ${call.request().url}")
                callback(UploadResult(false, "خطا در اتصال: ${e.message}"))
                
                // پاک کردن فایل بهینه‌شده
                if (optimizedFile != imageFile) {
                    optimizedFile.delete()
                }
            }
            
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string() ?: ""
                Log.d(TAG, "═══════════════════════════════════════")
                Log.d(TAG, "📥 پاسخ سرور: ${response.code}")
                Log.d(TAG, "📝 بدنه پاسخ: $responseBody")
                
                // پاک کردن فایل بهینه‌شده
                if (optimizedFile != imageFile) {
                    optimizedFile.delete()
                }
                
                if (response.isSuccessful) {
                    try {
                        val json = JSONObject(responseBody)
                        val message = json.optString("message", "موفق")
                        val imageUrl = json.optString("image_url", "")
                        
                        Log.d(TAG, "✅ آپلود موفق!")
                        Log.d(TAG, "🖼️ URL تصویر: $imageUrl")
                        
                        callback(UploadResult(true, message, imageUrl))
                    } catch (e: Exception) {
                        Log.e(TAG, "خطا در parse پاسخ: ${e.message}")
                        callback(UploadResult(true, "آپلود موفق"))
                    }
                } else {
                    Log.e(TAG, "❌ خطای سرور: ${response.code}")
                    callback(UploadResult(false, "خطای سرور: ${response.code}"))
                }
            }
        })
    }
    
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) 
            as? android.net.ConnectivityManager
        val network = connectivityManager?.activeNetwork
        val capabilities = connectivityManager?.getNetworkCapabilities(network)
        return capabilities?.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
    
    data class UploadResult(
        val success: Boolean,
        val message: String,
        val imageUrl: String = ""
    )
}

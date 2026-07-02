package com.example.insectdetector.utils

import android.content.Context
import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException

class ServerUploader(private val context: Context) {
    
    companion object {
        private const val TAG = "ServerUploader"
        
        // ✅ آدرس سرور Hugging Face
        const val SERVER_URL = "https://touraj732-insect-detector-server.hf.space"
    }
    
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val request = chain.request()
            Log.d(TAG, "🌐 ارسال درخواست: ${request.url}")
            chain.proceed(request)
        }
        .build()
    
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
        Log.d(TAG, "📏 حجم فایل: ${imageFile.length() / 1024} KB")
        Log.d(TAG, "🦋 گونه: $className")
        Log.d(TAG, "📊 اطمینان: $confidence")
        Log.d(TAG, "📍 مختصات: $latitude, $longitude")
        Log.d(TAG, "👤 کاربر: $userName")
        
        if (!imageFile.exists()) {
            Log.e(TAG, "❌ فایل تصویر وجود ندارد: ${imageFile.absolutePath}")
            callback(UploadResult(false, "فایل تصویر وجود ندارد"))
            return
        }
        
        if (!isNetworkAvailable()) {
            Log.e(TAG, "❌ اتصال اینترنت برقرار نیست")
            callback(UploadResult(false, "اتصال اینترنت برقرار نیست"))
            return
        }
        
        Log.d(TAG, "✅ اینترنت متصل است")
        
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", imageFile.name,
                imageFile.asRequestBody("image/jpeg".toMediaType()))
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
                Log.e(TAG, "❌ خطا در آپلود: ${e.message}", e)
                Log.e(TAG, "🔗 URL: ${call.request().url}")
                Log.e(TAG, "⏱️ Timeout ممکن است رخ داده باشد")
                callback(UploadResult(false, "خطا در اتصال: ${e.message}"))
            }
            
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string() ?: ""
                Log.d(TAG, "═══════════════════════════════════════")
                Log.d(TAG, "📥 پاسخ سرور: ${response.code}")
                Log.d(TAG, "📝 بدنه پاسخ: $responseBody")
                
                if (response.isSuccessful) {
                    try {
                        val json = JSONObject(responseBody)
                        val message = json.optString("message", "موفق")
                        val imageUrl = json.optString("image_url", "")
                        
                        Log.d(TAG, "✅ آپلود موفق!")
                        Log.d(TAG, "🖼️ URL تصویر: $imageUrl")
                        
                        callback(UploadResult(
                            success = true,
                            message = message,
                            imageUrl = imageUrl
                        ))
                    } catch (e: Exception) {
                        Log.e(TAG, "خطا در parse پاسخ: ${e.message}")
                        callback(UploadResult(true, "آپلود موفق (پاسخ نامعتبر)"))
                    }
                } else {
                    Log.e(TAG, "❌ خطای سرور: ${response.code}")
                    Log.e(TAG, "📝 جزئیات: $responseBody")
                    callback(UploadResult(false, "خطای سرور: ${response.code} - $responseBody"))
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

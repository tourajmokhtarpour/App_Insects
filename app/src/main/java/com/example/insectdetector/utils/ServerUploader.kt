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
        
        // ✅ آدرس سرور Hugging Face خود را اینجا وارد کنید
        // مثال: https://your-username-insect-detector-server.hf.space
        const val SERVER_URL = "https://touraj732-insect-detector-server.hf.space"
    }
    
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .build()
    
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
        // بررسی وجود فایل
        if (!imageFile.exists()) {
            callback(UploadResult(false, "فایل تصویر وجود ندارد"))
            return
        }
        
        // بررسی اتصال اینترنت
        if (!isNetworkAvailable()) {
            callback(UploadResult(false, "اتصال اینترنت برقرار نیست"))
            return
        }
        
        Log.d(TAG, "📤 شروع آپلود: $primaryKey")
        Log.d(TAG, "📍 آدرس سرور: $SERVER_URL")
        
        // ساخت درخواست Multipart
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
        
        // اجرای درخواست در background
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "❌ خطا در آپلود: ${e.message}", e)
                callback(UploadResult(false, "خطا در اتصال: ${e.message}"))
            }
            
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string() ?: ""
                Log.d(TAG, "📥 پاسخ سرور: ${response.code}")
                Log.d(TAG, " بدنه پاسخ: $responseBody")
                
                if (response.isSuccessful) {
                    try {
                        val json = JSONObject(responseBody)
                        val message = json.optString("message", "موفق")
                        val imageUrl = json.optString("image_url", "")
                        
                        callback(UploadResult(
                            success = true,
                            message = message,
                            imageUrl = imageUrl
                        ))
                    } catch (e: Exception) {
                        callback(UploadResult(true, "آپلود موفق (پاسخ نامعتبر)"))
                    }
                } else {
                    callback(UploadResult(false, "خطای سرور: ${response.code} - $responseBody"))
                }
            }
        })
    }
    
    /**
     * بررسی اتصال اینترنت
     */
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) 
            as? android.net.ConnectivityManager
        val network = connectivityManager?.activeNetwork
        val capabilities = connectivityManager?.getNetworkCapabilities(network)
        return capabilities?.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
    
    /**
     * نتیجه آپلود
     */
    data class UploadResult(
        val success: Boolean,
        val message: String,
        val imageUrl: String = ""
    )
}

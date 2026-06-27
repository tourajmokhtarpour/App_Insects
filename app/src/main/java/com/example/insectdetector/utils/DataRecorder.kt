package com.example.insectdetector.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class DataRecorder(private val context: Context) {
    
    companion object {
        private const val TAG = "DataRecorder"
        private const val FOLDER_NAME = "InsectRecords"
        private const val CSV_FILE_NAME = "records.csv"
        private const val CSV_HEADER = "ID,ClassName,Confidence,Latitude,Longitude,DateTime,UserName,ImagePath\n"
    }
    
    fun generatePrimaryKey(): String {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val random = (1000..9999).random()
        return "INS_${timestamp}_$random"
    }
    
    fun saveImage(
        bitmap: Bitmap,
        className: String,
        primaryKey: String
    ): String? {
        try {
            val folderName = className.replace(" ", "_")
                .replace("(", "")
                .replace(")", "")
                .replace("/", "_")
            
            val externalDir = context.getExternalFilesDir(null)
            val baseFolder = File(externalDir, FOLDER_NAME)
            val speciesFolder = File(baseFolder, folderName)
            
            if (!speciesFolder.exists()) {
                speciesFolder.mkdirs()
            }
            
            val fileName = "${folderName}_$primaryKey.jpg"
            val imageFile = File(speciesFolder, fileName)
            
            imageFile.outputStream().use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
            }
            
            Log.d(TAG, "تصویر ذخیره شد: ${imageFile.absolutePath}")
            return imageFile.absolutePath
            
        } catch (e: Exception) {
            Log.e(TAG, "خطا در ذخیره تصویر: ${e.message}", e)
            return null
        }
    }
    
    fun saveToCSV(
        primaryKey: String,
        className: String,
        confidence: Float,
        latitude: Double,
        longitude: Double,
        userName: String,
        imagePath: String
    ): Boolean {
        try {
            val externalDir = context.getExternalFilesDir(null)
            val baseFolder = File(externalDir, FOLDER_NAME)
            
            if (!baseFolder.exists()) {
                baseFolder.mkdirs()
            }
            
            val csvFile = File(baseFolder, CSV_FILE_NAME)
            val fileExists = csvFile.exists()
            
            FileWriter(csvFile, true).use { writer ->
                if (!fileExists) {
                    writer.write(CSV_HEADER)
                }
                
                val dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
                val confidencePercent = String.format("%.2f", confidence * 100)
                
                val row = "$primaryKey," +
                         "\"$className\"," +
                         "$confidencePercent," +
                         "$latitude," +
                         "$longitude," +
                         "\"$dateTime\"," +
                         "\"$userName\"," +
                         "\"$imagePath\"\n"
                
                writer.write(row)
            }
            
            Log.d(TAG, "اطلاعات در CSV ثبت شد: $primaryKey")
            return true
            
        } catch (e: Exception) {
            Log.e(TAG, "خطا در ثبت CSV: ${e.message}", e)
            return false
        }
    }
    
    fun recordDetection(
        bitmap: Bitmap,
        className: String,
        confidence: Float,
        latitude: Double,
        longitude: Double,
        userName: String
    ): RecordResult {
        val primaryKey = generatePrimaryKey()
        val imagePath = saveImage(bitmap, className, primaryKey)
        
        if (imagePath == null) {
            return RecordResult(false, primaryKey, "خطا در ذخیره تصویر")
        }
        
        val csvSuccess = saveToCSV(
            primaryKey = primaryKey,
            className = className,
            confidence = confidence,
            latitude = latitude,
            longitude = longitude,
            userName = userName,
            imagePath = imagePath
        )
        
        return if (csvSuccess) {
            RecordResult(true, primaryKey, "ثبت موفق")
        } else {
            RecordResult(false, primaryKey, "خطا در ثبت CSV")
        }
    }
    
    data class RecordResult(
        val success: Boolean,
        val primaryKey: String,
        val message: String
    )
}

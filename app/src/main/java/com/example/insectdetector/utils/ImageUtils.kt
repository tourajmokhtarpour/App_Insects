package com.example.insectdetector.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import java.io.InputStream

object ImageUtils {

    /**
     * بارگذاری Bitmap از Uri با اندازه بهینه
     */
    fun loadBitmapFromUri(context: Context, uri: Uri, maxSize: Int = 1024): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val bitmap = decodeSampledBitmap(inputStream, maxSize, maxSize)
            inputStream.close()
            
            // اصلاح چرخش تصویر
            val rotatedBitmap = fixRotation(context, uri, bitmap)
            rotatedBitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * بارگذاری Bitmap بهینه از InputStream
     */
    private fun decodeSampledBitmap(
        inputStream: InputStream,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {
        // مرحله 1: بررسی ابعاد
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        
        val byteArray = inputStream.readBytes()
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options)
        
        // مرحله 2: محاسبه inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options)
    }

    /**
     * محاسبه inSampleSize برای کاهش مصرف حافظه
     */
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val (height: Int, width: Int) = options.outHeight to options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    /**
     * اصلاح چرخش تصویر بر اساس EXIF
     */
    private fun fixRotation(context: Context, uri: Uri, bitmap: Bitmap): Bitmap {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return bitmap
        val exif = ExifInterface(inputStream)
        inputStream.close()

        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )

        val rotationDegrees = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }

        return if (rotationDegrees != 0f) {
            val matrix = Matrix().apply { postRotate(rotationDegrees) }
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } else {
            bitmap
        }
    }

    /**
     * تغییر اندازه Bitmap
     */
    fun resizeBitmap(bitmap: Bitmap, targetSize: Int): Bitmap {
        val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
        val newWidth: Int
        val newHeight: Int

        if (bitmap.width > bitmap.height) {
            newWidth = targetSize
            newHeight = (targetSize / aspectRatio).toInt()
        } else {
            newHeight = targetSize
            newWidth = (targetSize * aspectRatio).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    /**
     * تبدیل Bitmap به مربع (center crop)
     */
    fun centerCropToSquare(bitmap: Bitmap, size: Int): Bitmap {
        val minDim = minOf(bitmap.width, bitmap.height)
        val x = (bitmap.width - minDim) / 2
        val y = (bitmap.height - minDim) / 2
        val cropped = Bitmap.createBitmap(bitmap, x, y, minDim, minDim)
        return Bitmap.createScaledBitmap(cropped, size, size, true)
    }
}
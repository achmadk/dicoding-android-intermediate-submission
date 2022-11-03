package dev.achmadk.proasubmission1.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

suspend fun reduceImageFile(file: File, expectedSize: Int? = 1000000): File = withContext(Dispatchers.IO) {
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    var streamLength: Int
//    val compressFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Bitmap.CompressFormat.WEBP_LOSSLESS else Bitmap.CompressFormat.JPEG
    val compressFormat = Bitmap.CompressFormat.JPEG
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(compressFormat, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > expectedSize!!)
    bitmap.compress(compressFormat, compressQuality, FileOutputStream(file))
    return@withContext file
}
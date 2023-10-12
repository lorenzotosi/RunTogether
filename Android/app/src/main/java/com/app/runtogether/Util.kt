package com.app.runtogether

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class Util {
    companion object{
        suspend fun getImageByteArrayFromUri(uri: Uri, context: Context): ByteArray {
            return withContext(Dispatchers.IO) {
                val inputStream = context.contentResolver.openInputStream(uri)
                val buffer = inputStream?.readBytes() ?: ByteArray(0)
                inputStream?.close()
                buffer
            }
        }

        fun compressImageByteArray(imageByteArray: ByteArray, quality: Int, context: Context): ByteArray {
            try {
                val inputStream = ByteArrayInputStream(imageByteArray)
                val bitmap = BitmapFactory.decodeStream(inputStream)

                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)

                return byteArrayOutputStream.toByteArray()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return imageByteArray // Return the original ByteArray if there's an error
        }

    }
}
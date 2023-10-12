package com.app.runtogether

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
    }
}
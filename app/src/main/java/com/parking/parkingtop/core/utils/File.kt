package com.parking.parkingtop.core.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

/**
 * Copies the content behind a content:// Uri into a temp File
 * that OkHttp can read as a RequestBody.
 */
fun uriToFile(context: Context, uri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val mimeType = context.contentResolver.getType(uri)
        val extension = when {
            mimeType?.contains("png") == true  -> ".png"
            mimeType?.contains("webp") == true -> ".webp"
            else -> ".jpg"
        }
        val tempFile = File.createTempFile("profile_", extension, context.cacheDir)
        FileOutputStream(tempFile).use { output ->
            inputStream.copyTo(output)
        }
        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
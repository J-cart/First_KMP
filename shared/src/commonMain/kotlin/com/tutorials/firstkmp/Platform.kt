package com.tutorials.firstkmp

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect class PlatformUtil {
    fun copyToClipboard(textToCopy: String, toastMessage: String)
    fun shareText(text: String, intentTitle: String)

    fun createImagePicker():ImageUtil
}

expect class ImageUtil {
    @Composable
    fun registerPicker(onImagePicked: (ByteArray) -> Unit)

    fun pickImage()

    fun rememberImageBitmapFromByteArray(bytes:ByteArray?):ImageBitmap?

    suspend fun saveImage(bytes: ByteArray?):String

    suspend fun getImage(fileName: String):ByteArray?

    suspend fun deleteImage(fileName: String)
}
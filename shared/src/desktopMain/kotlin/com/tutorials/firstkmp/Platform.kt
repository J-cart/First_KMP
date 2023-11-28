package com.tutorials.firstkmp

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap



class DesktopPlatform : Platform {
    override val name: String = "Desktop"
}

actual fun getPlatform(): Platform = DesktopPlatform()
actual fun getViewPlatform(): Platform = DesktopPlatform()


actual class PlatformUtil {
    actual fun copyToClipboard(textToCopy: String, toastMessage: String) {

    }

    actual fun shareText(text: String, intentTitle: String) {

    }

    actual  fun createImagePicker():ImageUtil = ImageUtil()



}


actual class ImageUtil{

    @Composable
    actual fun registerPicker(onImagePicked: (ByteArray) -> Unit) {
    }

    @Composable
    actual fun initUtil() {

    }
    actual fun pickImage() {

    }

    @Composable
    actual fun registerOnImageCaptured(onImageCaptured: (ByteArray?) -> Unit) {
    }

    actual fun captureImage() {}

    actual fun rememberImageBitmapFromByteArray(bytes:ByteArray?): ImageBitmap?{
       return null
    }

    actual suspend fun saveImage(bytes: ByteArray?):String?{
        return null
    }

    actual suspend fun getImage(fileName: String): ImageBitmap?{
        return null
    }

    actual suspend fun deleteImage(fileName: String){

    }



}
package com.tutorials.firstkmp

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()
actual fun getViewPlatform(): Platform = IOSPlatform()

actual class PlatformUtil{
    actual fun copyToClipboard(textToCopy: String, toastMessage: String) {
        // TODO: platform copy logic here
    }
    actual fun shareText(text: String, intentTitle: String) {
        // TODO: platform share logic here
    }

   actual fun createImagePicker():ImageUtil{
       // TODO: initialize ImageUtil()
       return ImageUtil()
   }
}

actual class ImageUtil{
    @Composable
    actual fun initUtil() {
        // TODO: init platform image picker
    }

    @Composable
    actual fun registerPicker(onImagePicked: (ByteArray) -> Unit) {
        // TODO: platform image picker
    }


  actual  fun pickImage(){
      // TODO: platform image picker initialization
  }

    @Composable
    actual fun registerOnImageCaptured(onImageCaptured: (ByteArray?) -> Unit) {
    }

    actual fun captureImage() {}
    actual fun rememberImageBitmapFromByteArray(bytes: ByteArray?): ImageBitmap? {
        // TODO: convert byteArray to imageBitmap
        return null
    }

    actual suspend fun saveImage(bytes: ByteArray?):String?{
        // TODO: platform save image
        return ""
    }

    actual suspend fun getImage(fileName: String):ImageBitmap?{
        // TODO: platform getImage
        return null
    }

    actual suspend fun deleteImage(fileName: String){
        // TODO: platform delete image
    }


}
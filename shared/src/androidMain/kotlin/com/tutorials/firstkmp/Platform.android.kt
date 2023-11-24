package com.tutorials.firstkmp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()
actual fun getViewPlatform(): Platform = AndroidPlatform()


actual class PlatformUtil(private val context: Context) {
    actual fun copyToClipboard(textToCopy: String, toastMessage: String) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("First KMP", textToCopy)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
    }

    actual fun shareText(text: String, intentTitle: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(Intent.createChooser(shareIntent, intentTitle ?: "Share"))
    }

    actual  fun createImagePicker():ImageUtil = ImageUtil()



}


actual class ImageUtil{

    private lateinit var getContent: ActivityResultLauncher<String>

    @Composable
    actual fun registerPicker(onImagePicked: (ByteArray) -> Unit) {
        val context = LocalContext.current
        getContent =
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                uri?.let {
                    context.contentResolver.openInputStream(uri)?.use {
                        onImagePicked(it.readBytes())
                    }
                }

            }
    }
    actual fun pickImage() {
        getContent.launch("image/*")
    }

    actual fun rememberImageBitmapFromByteArray(bytes:ByteArray?): ImageBitmap?{
        return if (bytes != null){
            BitmapFactory.decodeByteArray(bytes,0,bytes.size).asImageBitmap()
        } else null
    }

    actual suspend fun saveImage(bytes: ByteArray?):String{
        return ""
    }

    actual suspend fun getImage(fileName: String):ByteArray?{
        return null
    }

    actual suspend fun deleteImage(fileName: String){

    }



}










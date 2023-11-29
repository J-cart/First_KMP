package com.tutorials.firstkmp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

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
    private lateinit var mContext: Context
    private lateinit var ioScope:CoroutineScope
    private lateinit var cameraLauncher: ManagedActivityResultLauncher<Void?, Bitmap?>

    @Composable
    actual fun initUtil(){
        val context = LocalContext.current
        ioScope = rememberCoroutineScope()
        mContext = context
    }

    @Composable
    actual fun registerPicker(onImagePicked: (ByteArray) -> Unit) {

        getContent =
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                uri?.let {
                    mContext.contentResolver.openInputStream(uri)?.use {
                        onImagePicked(it.readBytes())
                    }
                }

            }
    }
    actual fun pickImage() {
        getContent.launch("image/*")
    }

    @Composable
    actual fun registerOnImageCaptured(onImageCaptured:(ByteArray?)->Unit){
         cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()){result->
            result?.let {
                onImageCaptured(convertBitmapToByteArray(it))
                Log.d("JOENOTETAG", "bitmap-> $it")
            }?: Log.d("JOENOTETAG", "bitmap (null)-> $result")
        }
    }

    actual fun captureImage(){
        cameraLauncher.launch()
    }

    actual fun rememberImageBitmapFromByteArray(bytes:ByteArray?): ImageBitmap?{
        return if (bytes != null){
            BitmapFactory.decodeByteArray(bytes,0,bytes.size).asImageBitmap()
        } else null
    }

    actual suspend fun saveImage(bytes: ByteArray?):String?{
        var filePath:String? = null
        bytes?.let {imgBytes->
            createImageFile(mContext) {
                filePath = it.absolutePath
                filePath?.let {path->
                    saveBitmapToFile(imgBytes,path)
                }
            }
        }
        return filePath
    }

    actual suspend fun getImage(fileName: String):ImageBitmap?{
        return getBitmapFromFilePath(fileName)?.asImageBitmap()
    }

    actual suspend fun deleteImage(fileName: String){
        ioScope.launch (Dispatchers.IO){
            try {
                File(fileName).delete()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



}


private fun createImageFile(context: Context,onFileCreated:(File)->Unit) {
    val fileName = System.currentTimeMillis().toString() + ".jpg"
    val fileDir = File(context.filesDir, "images")
    if (!fileDir.exists()) fileDir.mkdir()
    val file = File(fileDir, fileName)
   onFileCreated(file)
}


fun getBitmapFromFilePath(
    filePath: String,
): Bitmap? {
    return BitmapFactory.decodeFile(filePath)
}


fun saveBitmapToFile(bytes:ByteArray, filePath: String) {
    val bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
    val outputStream = FileOutputStream(filePath)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
}

fun convertBitmapToByteArray(bitmap:Bitmap):ByteArray{
    val byteOS = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG,100, byteOS)
    return byteOS.toByteArray()
}


/*@Composable
fun CameraUtil(onLaunch:(ManagedActivityResultLauncher<Void?,Bitmap?>)->Unit,onImageCaptured:(Bitmap?)->Unit) {

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()){result->
       result?.let {
           onImageCaptured(it)
           Log.d("JOENOTETAG", "bitmap-> $it")
       }?: Log.d("JOENOTETAG", "bitmap (null)-> $result")
    }

    onLaunch(cameraLauncher)

}*/









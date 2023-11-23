package com.tutorials.firstkmp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast

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

}
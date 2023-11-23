package com.tutorials.firstkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect class PlatformUtil {
    fun copyToClipboard(textToCopy: String, toastMessage: String)
    fun shareText(text: String, intentTitle: String)
}
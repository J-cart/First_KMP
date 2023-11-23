package com.tutorials.firstkmp

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
}
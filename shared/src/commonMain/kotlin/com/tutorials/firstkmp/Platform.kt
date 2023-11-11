package com.tutorials.firstkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
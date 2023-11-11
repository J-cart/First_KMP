package com.tutorials.firstkmp

class Greeting {
    private val platform: Platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }

    fun anotherGreeting() =
         "Joseph is testing here"


}
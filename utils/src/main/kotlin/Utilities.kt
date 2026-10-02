package org.example.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class Printer(private val message: String) {

    fun printMessage() = runBlocking {
        println(message)

        delay(1000L)

        println("Message printed after 1 second")
    }
}
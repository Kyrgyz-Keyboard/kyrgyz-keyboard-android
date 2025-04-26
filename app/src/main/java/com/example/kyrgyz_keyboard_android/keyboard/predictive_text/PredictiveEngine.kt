package com.example.kyrgyz_keyboard_android.keyboard.predictive_text

import org.apertium.lttoolbox.LTProc

import java.io.StringReader

fun main() {
    val binaryKirData = "app/src/main/res/kir.automorf.bin"
    val input = StringReader("боюнча\n")
    val output = StringBuilder()

    LTProc.doMain(arrayOf(binaryKirData), input, output)

    println(output)
}

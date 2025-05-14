package com.example.kyrgyz_keyboard_android.keyboard.predictive_text

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

fun copyAssetToFile(context: Context, assetName: String, outputFileName: String): File {
    val outFile = File(context.filesDir, outputFileName)
    if (!outFile.exists()) {
        context.assets.open(assetName).use { input ->
            outFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }
    return outFile
}

fun mapFile(context: Context, file: File): MappedByteBuffer {
    val inputStream = FileInputStream(file)
    val channel = inputStream.channel
    return channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length())
}

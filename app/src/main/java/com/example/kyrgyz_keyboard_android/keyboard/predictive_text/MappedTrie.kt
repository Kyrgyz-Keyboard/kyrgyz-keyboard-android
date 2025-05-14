package com.example.kyrgyz_keyboard_android.keyboard.predictive_text

import java.nio.MappedByteBuffer

class MappedTrie(
    private val words: List<String>,
    private val buffer: MappedByteBuffer
) : PredictiveTextEngine {

    private val wordStartOffset: Int = 0

    @Volatile
    private var ready: Boolean = false

    init {
        buffer.position(wordStartOffset)

//        var wordsSkipped = 0
//        while (wordsSkipped < words.size) {
//            while (buffer.get() != 0.toByte()) {
//
//            }
//            wordsSkipped++
//        }

        ready = true
    }

    override fun getPredictions(prefix: String): List<WordPrediction> {
        if (!ready || prefix.isEmpty()) return emptyList()

        val result = mutableListOf<WordPrediction>()

        for ((i, word) in words.withIndex()) {
            if (word.startsWith(prefix)) {
                result.add(WordPrediction(word, freq = 1, isStem = false))
            }
            if (result.size >= 9) break
        }

        return result
    }

    fun isReady(): Boolean = ready
}

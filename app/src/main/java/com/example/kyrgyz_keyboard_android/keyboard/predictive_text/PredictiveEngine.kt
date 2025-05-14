package com.example.kyrgyz_keyboard_android.keyboard.predictive_text

import android.content.Context
import android.util.Log
import org.apertium.lttoolbox.process.FSTProcessor
import java.io.StringReader
import kotlinx.coroutines.*
import java.nio.MappedByteBuffer

class PredictiveTextEngineImpl(context: Context) : PredictiveTextEngine {
    private val binaryKirData = "kir.automorf.bin"
    private val fstp = FSTProcessor()
    private var trie: PredictiveTextEngine = Trie(emptyList())
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @Volatile
    private var ready = false


    init {
        scope.launch {
            ready = true
            try {
                val trieFile = copyAssetToFile(context, "trie.bin", "trie_mapped.bin")
                val buffer = mapFile(context, trieFile)

                val countBytes = ByteArray(3)
                buffer.get(countBytes)
                val wordCount = (countBytes[0].toInt() and 0xFF shl 16) or
                        (countBytes[1].toInt() and 0xFF shl 8) or
                        (countBytes[2].toInt() and 0xFF)

                val words = readWordsFromBuffer(buffer, wordCount, maxWords = 3000)
                trie = MappedTrie(words, buffer)
            } catch (e: Exception) {
                Log.e("PredictiveEngine", "Failed to load mapped trie", e)
                trie = Trie(emptyList())
            }
        }
    }

    fun readWordsFromBuffer(buffer: MappedByteBuffer, wordCount: Int, maxWords: Int): List<String> {
        val words = mutableListOf<String>()
        var read = 0
        while (read < minOf(wordCount, maxWords) && buffer.hasRemaining()) {
            val sb = StringBuilder()
            while (true) {
                if (!buffer.hasRemaining()) break
                val b = buffer.get()
                if (b == 0.toByte()) break
                sb.append(BYTE_TO_CHAR[b] ?: '?')
            }
            words.add(sb.toString())
            read++
        }
        return words
    }

    fun isReady(): Boolean = ready

    fun getWordStem(word: String): String {
        val output = StringBuilder()
        try {
            fstp.analysis(StringReader(word + '\n'), output)
        } catch (e: Exception) {
            Log.e("PredictiveEngine", "Error analyzing word: $word", e)
            return word
        }

        return output.toString()
            .removePrefix("^")
            .removeSuffix("$")
            .split("/")
            .map { reading ->
                reading.replace(" ", "").substringBefore("<")
            }
            .filter { base ->
                !base.startsWith("*")
            }
            .minOrNull() ?: word
    }

    override fun getPredictions(currentText: String): List<WordPrediction> = try {
        if (currentText.isEmpty()) {
            emptyList()
        } else {
            val predictions = trie.getPredictions(currentText)
            predictions.sortedByDescending { it.freq }
                .take(MAX_SUGGESTIONS)
        }
    } catch (e: Exception) {
        Log.e("PredictiveEngine", "Error getting predictions for: $currentText", e)
        emptyList()
    }

    companion object {
        private const val MAX_SUGGESTIONS = 5
    }
}
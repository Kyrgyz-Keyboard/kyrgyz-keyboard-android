package com.example.kyrgyz_keyboard_android.keyboard.predictive_text

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.apertium.lttoolbox.process.FSTProcessor
import java.io.StringReader

class PredictiveTextEngineImpl(context: Context) : PredictiveTextEngine {
    private val fstp = FSTProcessor()
    private var trie = Trie()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @Volatile
    private var ready = false

    init {
        scope.launch {
            ready = true

            val kirAutomorfFile = copyAssetToFile(
                context,
                "kir.automorf.bin",
                "kir.automorf_mapped.bin"
            )
            val kirAutomorfbuffer = mapFile(context, kirAutomorfFile)
            fstp.load(kirAutomorfbuffer, "kir.automorf_mapped.bin")
            fstp.initAnalysis()
            if (!fstp.valid()) {
                throw RuntimeException("Validity test for FSTProcessor failed")
            }

            val trieFile = copyAssetToFile(context, "trie.bin", "trie_mapped.bin")
            val trieBuffer = mapFile(context, trieFile)
            try {
                val countBytes = ByteArray(3)
                trieBuffer.get(countBytes)
                val wordCount = (countBytes[0].toInt() and 0xFF shl 16) or
                        (countBytes[1].toInt() and 0xFF shl 8) or
                        (countBytes[2].toInt() and 0xFF)

                Log.d("PredictiveEngine", "Word count: $wordCount")
                trie.readWordsFromBuffer(trieBuffer, wordCount)

            } catch (e: Exception) {
                Log.e("PredictiveEngine", "Failed to load trie", e)
            }

            val maxMemory = Runtime.getRuntime().maxMemory()
            val allocatedMemory = Runtime.getRuntime().totalMemory()
            val freeMemory = Runtime.getRuntime().freeMemory()

            Log.d("PredictiveEngine", "Max memory: ${maxMemory / 1024 / 1024} MB")
            Log.d("PredictiveEngine", "Allocated: ${allocatedMemory / 1024 / 1024} MB")
            Log.d("PredictiveEngine", "Free: ${freeMemory / 1024 / 1024} MB")
        }
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

    override fun getPredictions(currentText: String): List<String> = try {
        trie.getSimpleWordPredictions(currentText, MAX_SUGGESTIONS)
    } catch (e: Exception) {
        Log.e("PredictiveEngine", "Error getting predictions for: $currentText", e)
        emptyList()
    }

    companion object {
        private const val MAX_SUGGESTIONS = 5
    }
}
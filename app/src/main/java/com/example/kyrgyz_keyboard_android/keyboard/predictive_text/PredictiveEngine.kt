package com.example.kyrgyz_keyboard_android.keyboard.predictive_text

import android.content.Context
import android.util.Log
import org.apertium.lttoolbox.process.FSTProcessor
import java.io.StringReader
import java.nio.ByteBuffer

class PredictiveTextEngineImpl(context: Context) : PredictiveTextEngine {
    private val binaryKirData = "kir.automorf.bin"
    private val binaryTriData = "trie.bin"
    private val fstp = FSTProcessor()
    private var trie: Trie

    init {
        val assetManager = context.assets
        assetManager.open(binaryKirData).use { input ->
            val buffer = ByteArray(input.available())
            input.read(buffer)
            fstp.load(ByteBuffer.wrap(buffer), binaryKirData)
            fstp.initAnalysis()
            if (!fstp.valid()) {
                throw RuntimeException("Validity test for FSTProcessor failed")
            }
        }

        trie = try {
            Trie.load(context.assets.open(binaryTriData))
        } catch (e: Exception) {
            Trie(emptyList())
        }
    }

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
            // val predictions = trie.getPredictions(currentText)
            // predictions.sortedByDescending { it.freq }
            //     .take(MAX_SUGGESTIONS)
            listOf(
                WordPrediction("тест", 0, false),
                WordPrediction("тест2", 0, false),
                WordPrediction("тест3", 0, false),
                WordPrediction("тест4", 0, false),
                WordPrediction("тест5", 0, false)
            )
        }
    } catch (e: Exception) {
        Log.e("PredictiveEngine", "Error getting predictions for: $currentText", e)
        emptyList()
    }

    companion object {
        private const val MAX_SUGGESTIONS = 5
    }
}
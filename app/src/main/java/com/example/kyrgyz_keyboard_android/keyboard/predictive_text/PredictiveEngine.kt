package com.example.kyrgyz_keyboard_android.keyboard.predictive_text

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PredictiveTextEngineImpl(context: Context) : PredictiveTextEngine {
    private var trie = Trie()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    companion object {
        private const val MAX_SUGGESTIONS = 5
    }

    @Volatile
    private var ready = false

    init {
        scope.launch {
            val kirAutomorfFile = copyAssetToFile(
                context,
                "kir.automorf.bin",
                "kir.automorf_mapped.bin"
            )
            val kirAutomorfbuffer = mapFile(context, kirAutomorfFile)
            trie.loadFSTP(kirAutomorfbuffer, "kir.automorf_mapped.bin")

            val trieFile = copyAssetToFile(
                context,
                "trie.bin",
                "trie_mapped.bin"
            )
            val trieBuffer = mapFile(context, trieFile)
            try {
                trie.load(trieBuffer) {
                    ready = true
                }
            } catch (e: Exception) {
                Log.e("PredictiveEngine", "Failed to load trie", e)
            }
        }
    }

    fun isReady(): Boolean = ready

    fun reset() {}

    override fun getPredictions(inputText: String): List<String> {
        return try {
            val limitedText = if (inputText.length > 1000) inputText.takeLast(1000) else inputText
            
            val lastWord = limitedText.split(Regex("\\s+")).lastOrNull()?.trim() ?: ""

            val predictions = trie.getSimpleWordPredictions(lastWord, MAX_SUGGESTIONS)
            
            if (predictions.isEmpty()) {
                emptyList()
            } else {
                predictions
            }
        } catch (e: Exception) {
            Log.e("PredictiveEngine", "Error getting predictions for: $inputText", e)
            emptyList()
        }
    }
}
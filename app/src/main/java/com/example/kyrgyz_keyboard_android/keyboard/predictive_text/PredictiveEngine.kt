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

    override fun getPredictions(currentText: String): List<String> {
        return try {
            val currentWord = currentText.substringAfterLast(' ').trim()
            val rawPredictions = trie.fetch(currentWord, MAX_SUGGESTIONS)
            WordFilter.filterPredictions(currentWord, rawPredictions)
        } catch (e: Exception) {
            Log.e("PredictiveEngine", "Error getting predictions for: $currentText", e)
            emptyList()
        }
    }
}
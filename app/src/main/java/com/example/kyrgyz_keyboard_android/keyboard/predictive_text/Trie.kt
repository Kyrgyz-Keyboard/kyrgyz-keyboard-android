package com.example.kyrgyz_keyboard_android.keyboard.predictive_text

import java.io.InputStream

data class TrieNode(
    var freq: Int = 0,
    val children: MutableMap<Pair<Boolean, Int>, TrieNode> = mutableMapOf()
)

const val RETURN_MARKER: Int = 1 shl 7
const val STEM_MARKER: Int = 1 shl 6

val DECODING_TABLE = listOf(
    ',', '.', ':'
) + ('0'..'9') + ('a'..'z') + listOf(
    'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р',
    'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', 'ң', 'ү', 'ө'
)

val BYTE_TO_CHAR = DECODING_TABLE.mapIndexed { index, c -> (index + 1).toByte() to c }.toMap()

class Trie(private val words: List<String>) : PredictiveTextEngine {

    private val root = TrieNode()
    private val reverseIndex = words.mapIndexed { i, w -> i to w }.toMap()

    fun print() {
        fun dfs(node: TrieNode, depth: Int = 0) {
            for ((key, child) in node.children) {
                val (isStem, index) = key
                val word = reverseIndex[index] ?: "<?>"
                println("  ".repeat(depth) + "- $word (${if (isStem) "stem" else "step"}): freq=${child.freq}")
                dfs(child, depth + 1)
            }
        }
        dfs(root)
    }

    override fun getPredictions(currentText: String): List<WordPrediction> {
        if (currentText.length < 2) return emptyList()
        
        val prefix = currentText.trim()
        val results = mutableListOf<WordPrediction>()
        
        var processedCount = 0
        for ((key, child) in root.children) {
            if (processedCount >= 80) break
            
            val (isStem, wordIndex) = key
            val word = words.getOrNull(wordIndex) ?: continue
            if (word.startsWith(prefix)) {
                results.add(WordPrediction(word, child.freq, isStem))
            }
            processedCount++
        }
        
        return results.sortedByDescending { it.freq }.take(5)
    }
}
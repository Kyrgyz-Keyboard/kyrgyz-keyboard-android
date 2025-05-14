package com.example.kyrgyz_keyboard_android.keyboard.predictive_text
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import com.example.kyrgyz_keyboard_android.keyboard.predictive_text.WordPrediction
data class TrieNode(
    var freq: Int = 0,
    val children: MutableMap<Pair<Boolean, Int>, TrieNode> = mutableMapOf()
)
const val RETURN_MARKER: Int = 1 shl 7
const val STEM_MARKER: Int = 1 shl 6
val DECODING_TABLE = listOf(
    ',', '.', ':'
) + ('0'..'9') + ('a'..'z') + listOf(
    'а','б','в','г','д','е','ё','ж','з','и','й','к','л','м','н','о','п','р','с','т','у','ф','х','ц','ч','ш','щ','ъ','ы','ь','э','ю','я',
    'ң','ү','ө'
)
val BYTE_TO_CHAR = DECODING_TABLE.mapIndexed { index, c -> (index + 1).toByte() to c }.toMap()
class Trie(private val words: List<String>) {
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
    fun getPredictions(currentText: String): List<WordPrediction> {
        val prefix = currentText.trim()
        val results = mutableListOf<WordPrediction>()
        for ((key, child) in root.children) {
            val (isStem, wordIndex) = key
            val word = words.getOrNull(wordIndex) ?: continue
            if (word.startsWith(prefix)) {
                results.add(WordPrediction(word, child.freq, isStem))
            }
        }
        return results.take(5)
    }
    fun getNextWordPredictions(previousWords: String): List<WordPrediction> {
        val inputWords = previousWords.trim().split(" ").filter { it.isNotEmpty() }
        var node = root
        for (word in inputWords) {
            val idx = words.indexOf(word)
            if (idx == -1) return emptyList()
            val key = node.children.keys.find { it.second == idx }
            if (key != null) {
                node = node.children[key]!!
            } else {
                return emptyList()
            }
        }
        return node.children.entries.withIndex().mapNotNull { (i, entry) ->
            val (key, child) = entry
            val (isStem, wordIndex) = key
            val word = words.getOrNull(wordIndex) ?: return@mapNotNull null
            val fullText = inputWords + word
            WordPrediction(
                word = fullText.joinToString(" "),
                freq = child.freq,
                isStem = isStem,
            )
        }.sortedWith(
            compareBy<WordPrediction> { it.isStem }
                .thenByDescending { it.word.split(" ").size }
                .thenBy{1}
        ).take(5)
    }
    companion object {
        fun load(input: InputStream): Trie {
            val countBytes = ByteArray(3)
            input.read(countBytes)
            val wordCount = (countBytes[0].toInt() and 0xFF shl 16) or
                    (countBytes[1].toInt() and 0xFF shl 8) or
                    (countBytes[2].toInt() and 0xFF)
            val words = mutableListOf<String>()
            repeat(wordCount) {
                val sb = StringBuilder()
                while (true) {
                    val b = input.read().toByte()
                    if (b == 0.toByte()) break
                    sb.append(BYTE_TO_CHAR[b] ?: '?')
                }
                words.add(sb.toString())
            }
            val trie = Trie(words)
            loadNode(trie.root, input)
            return trie
        }
        private fun loadNode(root: TrieNode, input: InputStream) {
            val stack = ArrayDeque<Pair<TrieNode, Int>>() // Stack of (node, layer)
            stack.addLast(root to 1)

            while (stack.isNotEmpty()) {
                val (current, layer) = stack.last()

                val byte1 = input.read()
                if (byte1 == -1) break
                val b = byte1.toByte()

                if (b.toInt() and RETURN_MARKER != 0) {
                    stack.removeLast()
                    continue
                }

                val isStem = b.toInt() and STEM_MARKER.toInt() != 0
                val high = b.toInt() and (RETURN_MARKER or STEM_MARKER).inv()

                val byte2 = input.read()
                val byte3 = input.read()
                if (byte2 == -1 || byte3 == -1) break

                val wordIndex = (high shl 16) or (byte2 shl 8) or byte3
                val child = TrieNode()
                current.children[Pair(isStem, wordIndex)] = child

                if (layer < 4) {
                    stack.addLast(child to (layer + 1))
                }
            }
        }
    }
}
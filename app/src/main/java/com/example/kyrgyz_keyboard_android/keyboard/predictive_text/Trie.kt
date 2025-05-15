package com.example.kyrgyz_keyboard_android.keyboard.predictive_text

import android.util.Log
import java.io.InputStream
import java.nio.MappedByteBuffer
import java.util.Collections.synchronizedList

const val RETURN_MARKER: Int = 1 shl 7
const val STEM_MARKER: Int = 1 shl 6

val DECODING_TABLE = listOf(
    ',', '.', ':'
) + ('0'..'9') + ('a'..'z') + listOf(
    'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м',
    'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ',
    'ы', 'ь', 'э', 'ю', 'я', 'ң', 'ү', 'ө'
)

val BYTE_TO_CHAR = DECODING_TABLE.mapIndexed { index, c -> (index + 1).toByte() to c }.toMap()

class Trie(private val words: MutableList<String> = synchronizedList(mutableListOf())) {

    private val root = mutableMapOf<Pair<Boolean, Int>, MutableMap<*, *>>()
    // private val reverseIndex = words.mapIndexed { i, w -> i to w }.toMap()

    // fun print() {
    //     fun dfs(node: MutableMap<Pair<Boolean, Int>, *>, depth: Int = 0) {
    //         for ((key, childAny) in node) {
    //             @Suppress("UNCHECKED_CAST")
    //             val child = childAny as? MutableMap<Pair<Boolean, Int>, *> ?: continue
    //             val (isStem, index) = key
    //             val word = reverseIndex[index] ?: "<?>"
    //             println("  ".repeat(depth) + "- $word (${if (isStem) "stem" else "step"})")
    //             dfs(child, depth + 1)
    //         }
    //     }
    //     dfs(root)
    // }

    fun getSimpleWordPredictions(currentText: String, maxResults: Int): List<String> {
        Log.d("Trie", "getPredictions: $currentText")
        val currentWords = currentText.split(" ")
        if (currentWords.isEmpty()) return emptyList()

        val results = mutableListOf<String>()
        synchronized(words) {
            for (word in words) {
                if (word.startsWith(currentWords.last())) {
                    results.add(word)
                    if (results.size >= maxResults) {
                        break
                    }
                }
            }
        }
        return results
    }

    fun load(input: MappedByteBuffer) {
        Log.d("PredictiveEngine", "Trie loading...")
        val zeroByte = 0.toByte()

        val countBytes = ByteArray(3)
        input.get(countBytes)
        val wordCount = (countBytes[0].toInt() and 0xFF shl 16) or
                (countBytes[1].toInt() and 0xFF shl 8) or
                (countBytes[2].toInt() and 0xFF)
        Log.d("PredictiveEngine", "Trie has $wordCount words")

        repeat(wordCount) {
            val sb = StringBuilder()
            while (true) {
                val b = input.get()
                if (b == zeroByte) break
                sb.append(BYTE_TO_CHAR[b])
            }
            words.add(sb.toString())
        }

        Log.d("PredictiveEngine", "Trie loaded ${words.size} words")

        // loadNode(trie.root, input)
    }

    private fun loadNode(
        root: MutableMap<Pair<Boolean, Int>, MutableMap<*, *>>,
        input: InputStream
    ) {
        val stack = ArrayDeque<Pair<MutableMap<Pair<Boolean, Int>, MutableMap<*, *>>, Int>>()
        stack.addLast(root to 1)

        while (stack.isNotEmpty()) {
            val (current, layer) = stack.last()

            val byte1 = input.read()
            if (byte1 == -1) break

            if ((byte1 and RETURN_MARKER) != 0) {
                stack.removeLast()
                continue
            }

            val isStem = (byte1 and STEM_MARKER) != 0
            val high = byte1 and STEM_MARKER.inv()

            val wordIndex = (high shl 16) or (input.read() shl 8) or input.read()
            val child = mutableMapOf<Pair<Boolean, Int>, MutableMap<*, *>>()
            current[Pair(isStem, wordIndex)] = child

            if (layer < 4) {
                stack.addLast(child to (layer + 1))
            }
        }
    }
}
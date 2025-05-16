package com.example.kyrgyz_keyboard_android.keyboard.predictive_text

import android.util.Log
import org.apertium.lttoolbox.process.FSTProcessor
import java.io.StringReader
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
    private val fstp = FSTProcessor()

    companion object {
        private const val MAX_LAYERS = 4
    }

    private val data = mutableMapOf<Pair<Boolean, Int>, MutableMap<*, *>>()
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
    //     dfs(data)
    // }

    private fun printMemoryUsage() {
        val maxMemory = Runtime.getRuntime().maxMemory()
        val allocatedMemory = Runtime.getRuntime().totalMemory()
        val freeMemory = Runtime.getRuntime().freeMemory()

        Log.d(
            "PredictiveEngine",
            "Memory stats:" +
                    "Max: ${maxMemory / 1024 / 1024} MB | " +
                    "Allocated: ${allocatedMemory / 1024 / 1024} MB | " +
                    "Free: ${freeMemory / 1024 / 1024} MB"
        )
    }

    fun getSimpleWordPredictions(currentText: String, maxResults: Int): List<String> {
        val currentWords = currentText.split(' ').filter { it.isNotBlank() }
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
        Log.d("PredictiveEngine", "Simple Word Prediction: $currentText -> $results")
        return results
    }

    fun loadFSTP(kirAutomorfbuffer: MappedByteBuffer, fileName: String) {
        try {
            fstp.load(kirAutomorfbuffer, fileName)
            fstp.initAnalysis()
            if (!fstp.valid()) {
                throw RuntimeException("Validity test for FSTProcessor failed")
            }
            Log.d("PredictiveEngine", "FSTP loaded")
        } catch (e: Exception) {
            Log.e("PredictiveEngine", "Failed to load FSTP", e)
        }
    }

    private fun getWordStem(word: String): String {
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

    private fun loadWords(count: Int, input: MappedByteBuffer) {
        val zeroByte = 0.toByte()
        repeat(count) {
            val sb = StringBuilder()
            while (true) {
                val b = input.get()
                if (b == zeroByte) break
                sb.append(BYTE_TO_CHAR[b])
            }
            words.add(sb.toString())
        }
    }

    fun load(input: MappedByteBuffer, onPreReady: (() -> Unit)? = null) {
        Log.d("PredictiveEngine", "Trie loading...")

        val countBytes = ByteArray(3)
        input.get(countBytes)
        val wordCount = (countBytes[0].toInt() and 0xFF shl 16) or
                (countBytes[1].toInt() and 0xFF shl 8) or
                (countBytes[2].toInt() and 0xFF)
        Log.d("PredictiveEngine", "Trie has $wordCount words")

        loadWords(100_000, input)
        onPreReady?.invoke()
        loadWords(wordCount - 100_000, input)
        Log.d("PredictiveEngine", "Trie loaded ${words.size} words")

        printMemoryUsage()

        Log.d("PredictiveEngine", "Trie is loading tree...")
        try {
            val stack = ArrayDeque<Pair<MutableMap<Pair<Boolean, Int>, MutableMap<*, *>>, Int>>()
            stack.addLast(data to 1)

            // while (stack.isNotEmpty()) {
            //     val (current, layer) = stack.last()
            //
            //     if (layer == 1 && data.size % 100 == 0) {
            //         Log.d("PredictiveEngine", "Words on layer 1: ${data.size}")
            //         printMemoryUsage()
            //     }
            //
            //     val byte1 = input.get().toInt()
            //     if ((byte1 and RETURN_MARKER) != 0) {
            //         stack.removeLast()
            //         continue
            //     }
            //
            //     val isStem = (byte1 and STEM_MARKER) != 0
            //     val high = byte1 and STEM_MARKER.inv()
            //
            //     val wordIndex = (high shl 16) or (input.get().toInt() shl 8) or input.get().toInt()
            //     val child = mutableMapOf<Pair<Boolean, Int>, MutableMap<*, *>>()
            //     current[Pair(isStem, wordIndex)] = child
            //
            //     if (layer < MAX_LAYERS) {
            //         stack.addLast(child to (layer + 1))
            //     }
            // }
            // Log.d("PredictiveEngine", "Trie load finished")

        } catch (e: OutOfMemoryError) {
            Log.e("PredictiveEngine", "Trie load failed: OutOfMemoryError", e)
        } catch (e: Exception) {
            Log.e("PredictiveEngine", "Trie load failed", e)
        }

        printMemoryUsage()
    }
}
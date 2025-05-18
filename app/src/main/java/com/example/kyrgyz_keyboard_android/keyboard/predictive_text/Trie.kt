package com.example.kyrgyz_keyboard_android.keyboard.predictive_text

import android.util.ArrayMap
import android.util.Log
import android.util.SparseArray
import androidx.core.util.size
import org.apertium.lttoolbox.process.FSTProcessor
import java.io.StringReader
import java.nio.MappedByteBuffer
import java.util.Collections.synchronizedList
import java.util.Collections.synchronizedMap

const val RETURN_MARKER: Int = 1 shl 7

val DECODING_TABLE = listOf(
    ',', '.', ':'
) + ('0'..'9') + ('a'..'z') + listOf(
    'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м',
    'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ',
    'ы', 'ь', 'э', 'ю', 'я', 'ң', 'ү', 'ө'
)

val ALL_WORD_CHARS =
    DECODING_TABLE.filter { it != ',' && it != '.' && it != ':' } +
            DECODING_TABLE.filter { it != ',' && it != '.' && it != ':' }
                .map { it.uppercaseChar() }

val BYTE_TO_CHAR = DECODING_TABLE.mapIndexed { index, c -> (index + 1).toByte() to c }.toMap()

class Trie {
    private val wordsIndexed: MutableMap<String, Int> = synchronizedMap(mutableMapOf())
    private val wordsIndexedReverse: MutableList<String> = synchronizedList(mutableListOf())
    private val data = SparseArray<SparseArray<*>>()

    private val fstp = FSTProcessor()

    companion object {
        private const val MAX_LAYERS = 4
        private const val WORD_INDEX_SHIFT = 1
    }

    // fun getData() {
    //     fun dfs(node: MutableMap<Int, *>, depth: Int = 0) {
    //         for ((key, childAny) in node) {
    //             @Suppress("UNCHECKED_CAST")
    //             val child = childAny as? MutableMap<Int, *> ?: continue
    //             val index = key
    //             val word = reverseIndex[index] ?: "<?>"
    //             println("  ".repeat(depth) + "- $word")
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

    // fun getSimpleWordPredictions(text: String, maxResults: Int): List<String> {
    //     val lastWord = text.split(' ').filter { it.isNotBlank() }.lastOrNull() ?: return emptyList()
    //
    //     val results = mutableListOf<String>()
    //     synchronized(wordsIndexedReverse) {
    //         for (word in wordsIndexedReverse) {
    //             if (word.startsWith(lastWord)) {
    //                 results.add(word)
    //                 if (results.size >= maxResults) {
    //                     break
    //                 }
    //             }
    //         }
    //     }
    //     Log.d("PredictiveEngine", "Simple Word Prediction: $text -> $results")
    //     return results
    // }

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

    fun fetch(text: String, maxResults: Int = 10): List<String> {
        val isMidWord = text.lastOrNull()?.let { ALL_WORD_CHARS.contains(it) } ?: false
        val words: List<Pair<String, String>> = text.split(' ')
            .filter { it.isNotBlank() }
            .map { word ->
                Pair(word, getWordStem(word))
            }

        fun fetchInner(curData: SparseArray<SparseArray<*>>, startIndex: Int): Sequence<Int> =
            sequence {
                if (startIndex == words.size) {
                    // Log.d("PredictiveEngine", "Results: $results")
                    for (index in 0 until curData.size()) {
                        yield(curData.keyAt(index))
                    }
                    return@sequence
                }

                val (word, apertiumWord) = words[startIndex]

                if (isMidWord && startIndex == words.size - 1) {
                    // Log.d("PredictiveEngine", "Results: $results")
                    for (index in 0 until curData.size) {
                        val wordIndex = curData.keyAt(index)
                        val prediction = wordsIndexedReverse[wordIndex]
                        if (prediction.startsWith(word)) {
                            yield(wordIndex)
                        }
                    }
                    return@sequence
                }

                wordsIndexed[word]?.let { wordIndex ->
                    val nextLayer = curData[wordIndex]
                    if (nextLayer != null) {
                        Log.d("PredictiveEngine", "Word found on layer (normal): \"$word\"")
                        yieldAll(
                            fetchInner(
                                nextLayer as SparseArray<SparseArray<*>>,
                                startIndex + 1
                            )
                        )
                    } else {
                        Log.d("PredictiveEngine", "Word not found on layer (normal): \"$word\"")
                    }
                } ?: Log.d("PredictiveEngine", "Unknown word: \"$word\"")

                wordsIndexed[apertiumWord]?.let { wordIndex ->
                    val nextLayer = curData[wordIndex]
                    if (nextLayer != null) {
                        Log.d(
                            "PredictiveEngine",
                            "Word found on layer (apertium): \"$apertiumWord\""
                        )
                        yieldAll(
                            fetchInner(
                                nextLayer as SparseArray<SparseArray<*>>,
                                startIndex + 1
                            )
                        )
                    } else {
                        Log.d(
                            "PredictiveEngine",
                            "Word not found on layer (apertium): \"$apertiumWord\""
                        )
                    }
                } ?: Log.d("PredictiveEngine", "Unknown word (apertium): \"$apertiumWord\"")
            }

        val distinctResults = mutableSetOf<String>()

        for (layersNum in minOf(words.size + 1, MAX_LAYERS) downTo 2) {
            Log.d("PredictiveEngine", "$layersNum, ${words.size - layersNum + 1}")
            for (prediction in fetchInner(data, words.size - layersNum + 1)) {
                distinctResults.add(wordsIndexedReverse[prediction])
                if (distinctResults.size >= maxResults) break
            }
            if (distinctResults.size >= maxResults) break
        }

        Log.d("PredictiveEngine", "Normal Predictions: $text -> $distinctResults")

        if (isMidWord && distinctResults.size < maxResults) {
            val lastWord =
                text.split(' ').filter { it.isNotBlank() }.lastOrNull() ?: return emptyList()

            synchronized(wordsIndexedReverse) {
                for (word in wordsIndexedReverse) {
                    if (word.startsWith(lastWord)) {
                        distinctResults.add(word)
                        if (distinctResults.size == maxResults) break
                    }
                }
            }
            Log.d("PredictiveEngine", "Including Simple Word Prediction: $text -> $distinctResults")
        }

        return distinctResults.toList()
    }

    private fun loadWords(count: Int, input: MappedByteBuffer) {
        val zeroByte = 0.toByte()
        val sb = StringBuilder()
        repeat(count) {
            while (true) {
                val b = input.get()
                if (b == zeroByte) break
                sb.append(BYTE_TO_CHAR[b])
            }
            wordsIndexed[sb.toString()] = it + WORD_INDEX_SHIFT
            wordsIndexedReverse.add(sb.toString())
            sb.clear()
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

        wordsIndexedReverse.clear()
        wordsIndexed.clear()
        repeat(WORD_INDEX_SHIFT) {
            wordsIndexedReverse.add("")
        }

        loadWords(100_000, input)
        onPreReady?.invoke()
        Log.d("PredictiveEngine", "Trie loaded ${wordsIndexed.size} words")
        loadWords(wordCount - 100_000, input)
        Log.d("PredictiveEngine", "Trie loaded ${wordsIndexed.size} words")

        printMemoryUsage()

        Log.d("PredictiveEngine", "Trie is loading tree...")
        try {
            val stack = ArrayDeque<Pair<SparseArray<SparseArray<*>>, Int>>()
            stack.addLast(data to 1)

            var byte1: Byte
            var wordIndex: Int
            var child: SparseArray<SparseArray<*>>
            var layer: Int

            while (stack.isNotEmpty()) {
                byte1 = input.get()
                if ((byte1.toInt() and RETURN_MARKER) != 0) {
                    stack.removeLast()
                    continue
                }

                child = SparseArray()
                wordIndex = (byte1.toUByte().toInt() shl 16) or
                        (input.get().toUByte().toInt() shl 8) or
                        input.get().toUByte().toInt()
                stack.last().first[wordIndex] = child

                layer = stack.last().second

                if (layer == 1 && data.size % 10_000 == 0) {
                    Log.d("PredictiveEngine", "Words on layer 1: ${data.size}")
                    printMemoryUsage()
                }

                if (layer < MAX_LAYERS) {
                    stack.addLast(child to (layer + 1))
                }
            }
            Log.d("PredictiveEngine", "Trie load finished")

        } catch (e: OutOfMemoryError) {
            Log.e("PredictiveEngine", "Trie load failed: OutOfMemoryError", e)
        } catch (e: Exception) {
            Log.e("PredictiveEngine", "Trie load failed", e)
        }

        printMemoryUsage()
    }
}
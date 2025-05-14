package com.example.kyrgyz_keyboard_android.keyboard.predictive_text

import org.apertium.lttoolbox.process.FSTProcessor
import org.apertium.utils.IOUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class PredictiveTextEngineImpl : PredictiveTextEngine {

    private val binaryKirData = "app/src/main/res/kir.automorf.bin"
    private val fstp = FSTProcessor()
    private val trie: Trie

    init {
        fstp.load(IOUtils.openFileAsByteBuffer(binaryKirData), binaryKirData)
        fstp.initAnalysis()
        if (!fstp.valid()) {
            throw RuntimeException("Validity test for FSTProcessor failed")
        }

        val file = File("app/src/main/res/trie.bin")
        if (!file.exists()) {
            throw FileNotFoundException("trie.bin not found")
        }
        trie = Trie.load(FileInputStream(file))
    }

    fun getWordStem(word: String): String {
        val output = StringBuilder()
        // fstp.analysis(StringReader(word + '\n'), output)

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

    override fun getPredictions(currentText: String): List<WordPrediction> =
        trie.getPredictions(currentText)

    // override fun getNextWordPredictions(previousWords: String): List<WordPrediction> =
    //     trie.getNextWordPredictions(previousWords)
}

fun main() {
    // LTProc.doMain(arrayOf(binaryKirData), input, output)

    val predictiveEngine = PredictiveTextEngineImpl()
    println(predictiveEngine.getWordStem("в"))
    println(predictiveEngine.getWordStem("экрандарынын"))
}
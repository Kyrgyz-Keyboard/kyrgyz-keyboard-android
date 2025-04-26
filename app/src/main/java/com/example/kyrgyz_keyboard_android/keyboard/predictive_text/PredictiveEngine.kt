package com.example.kyrgyz_keyboard_android.keyboard.predictive_text

import org.apertium.lttoolbox.process.FSTProcessor
import org.apertium.utils.IOUtils
import java.io.StringReader

class PredictiveTextEngineImpl : PredictiveTextEngine {

    private val binaryKirData = "app/src/main/res/kir.automorf.bin"
    private val fstp = FSTProcessor()

    init {
        fstp.load(IOUtils.openFileAsByteBuffer(binaryKirData), binaryKirData)
        fstp.initAnalysis()
        if (!fstp.valid()) {
            throw RuntimeException("Validity test for FSTProcessor failed")
        }
    }

    fun getWordStem(word: String): String {
        val output = StringBuilder()
        fstp.analysis(StringReader(word + '\n'), output)

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

    override fun getPredictions(currentText: String): List<WordPrediction> {
        TODO("Not yet implemented")
    }

    override fun getNextWordPredictions(previousWords: String): List<WordPrediction> {
        TODO("Not yet implemented")
    }
}

fun main() {
    // LTProc.doMain(arrayOf(binaryKirData), input, output)

    val predictiveEngine = PredictiveTextEngineImpl()
    println(predictiveEngine.getWordStem("боюнча"))
    println(predictiveEngine.getWordStem("экрандарынын"))
}

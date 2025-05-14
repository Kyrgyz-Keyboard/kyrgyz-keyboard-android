package com.example.kyrgyz_keyboard_android.keyboard.predictive_text

class WordPrediction(
    val word: String,
    val isStem: Boolean
)

interface PredictiveTextEngine {
    fun getPredictions(currentText: String): List<WordPrediction>
    // fun getNextWordPredictions(previousWords: String): List<WordPrediction>
}
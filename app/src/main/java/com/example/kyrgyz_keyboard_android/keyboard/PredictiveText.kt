package com.example.kyrgyz_keyboard_android.keyboard

data class WordPrediction(
    val word: String,
    val confidence: Float
)

interface PredictiveTextEngine {
    fun getPredictions(currentText: String): List<WordPrediction>
    fun getNextWordPredictions(previousWords: String): List<WordPrediction>
}
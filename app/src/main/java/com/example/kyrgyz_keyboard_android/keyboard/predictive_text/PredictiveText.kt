package com.example.kyrgyz_keyboard_android.keyboard.predictive_text

interface PredictiveTextEngine {
    fun getPredictions(currentText: String): List<String>
    // fun getNextWordPredictions(previousWords: String): List<String>
}
package com.example.kyrgyz_keyboard_android.keyboard.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class KeyboardViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KeyboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KeyboardViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
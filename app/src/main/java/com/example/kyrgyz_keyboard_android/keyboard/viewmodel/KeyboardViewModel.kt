package com.example.kyrgyz_keyboard_android.keyboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kyrgyz_keyboard_android.keyboard.model.CapsLockState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class KeyboardViewModel : ViewModel() {
    private val _capsLockState = MutableStateFlow(CapsLockState.OFF)
    val capsLockState: StateFlow<CapsLockState> = _capsLockState.asStateFlow()

    private val _currentWord = MutableStateFlow("")
    val currentWord: StateFlow<String> = _currentWord.asStateFlow()

    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions: StateFlow<List<String>> = _suggestions.asStateFlow()

    private val _recentWords = MutableStateFlow<List<String>>(emptyList())
    val recentWords: StateFlow<List<String>> = _recentWords.asStateFlow()

    fun updateCapsLockState(newState: CapsLockState) {
        viewModelScope.launch {
            _capsLockState.value = newState
        }
    }

    fun onSuggestionSelected(suggestion: String) {
        viewModelScope.launch {
            val currentLength = _currentWord.value.length
            repeat(currentLength) {
                onBackspace()
            }

            _currentWord.value = suggestion
            onWordComplete(suggestion)
            onTextInput(" ")
        }
    }

    fun onTextInput(text: String) {
        viewModelScope.launch {
            if (text == " ") {
                onWordComplete()
            } else {
                _currentWord.value += text
                updateSuggestions()
            }
        }
    }

    fun onBackspace() {
        viewModelScope.launch {
            if (_currentWord.value.isNotEmpty()) {
                _currentWord.value = _currentWord.value.dropLast(1)
                updateSuggestions()
            }
        }
    }

    fun onWordComplete(word: String = _currentWord.value) {
        viewModelScope.launch {
            if (word.isNotEmpty()) {
                updateRecentWords(word)
                _currentWord.value = ""
                _suggestions.value = emptyList()
            }
        }
    }

    private suspend fun updateSuggestions() {
        val currentWord = _currentWord.value.lowercase()
        _suggestions.value = if (currentWord.isNotEmpty()) {
            _recentWords.value
                .filter { it.lowercase().startsWith(currentWord) } // Changed from contains to startsWith
                .take(MAX_SUGGESTIONS)
        } else {
            emptyList()
        }
    }

    private fun updateRecentWords(newWord: String) {
        val currentRecent = _recentWords.value.toMutableList()
        currentRecent.remove(newWord) // Remove if exists
        currentRecent.add(0, newWord) // Add to beginning
        _recentWords.value = currentRecent.take(MAX_RECENT_WORDS)
    }

    // For future implementation
    fun loadDictionary() {
        viewModelScope.launch {
            // TODO: Load dictionary data
        }
    }

    fun trainPredictiveModel(text: String) {
        viewModelScope.launch {
            // TODO: Update prediction model with new text
        }
    }

    companion object {
        private const val MAX_SUGGESTIONS = 3
        private const val MAX_RECENT_WORDS = 4
    }
}
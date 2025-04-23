package com.example.kyrgyz_keyboard_android.keyboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kyrgyz_keyboard_android.keyboard.DummyPredictiveEngine
import com.example.kyrgyz_keyboard_android.keyboard.model.CapsLockState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class KeyboardViewModel : ViewModel() {
    private val predictiveEngine = DummyPredictiveEngine()

    private val _capsLockState = MutableStateFlow(CapsLockState.OFF)
    val capsLockState: StateFlow<CapsLockState> = _capsLockState.asStateFlow()

    private val _currentWord = MutableStateFlow("")
    val currentWord: StateFlow<String> = _currentWord.asStateFlow()

    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions: StateFlow<List<String>> = _suggestions.asStateFlow()

    private val _inputBuffer = MutableStateFlow("")
    val inputBuffer: StateFlow<String> = _inputBuffer.asStateFlow()

    private val _isMidWord = MutableStateFlow(false)
    val isMidWord: StateFlow<Boolean> = _isMidWord.asStateFlow()

    private val _isSymbolsMode = MutableStateFlow(false)
    val isSymbolsMode: StateFlow<Boolean> = _isSymbolsMode.asStateFlow()

    init {
        updateSuggestions()
    }

    fun toggleSymbolsMode() {
        viewModelScope.launch {
            _isSymbolsMode.value = !_isSymbolsMode.value
        }
    }

    fun updateCapsLockState(newState: CapsLockState) {
        viewModelScope.launch {
            _capsLockState.value = newState
        }
    }

    private fun updateSuggestions() {
        if (_isMidWord.value) {
            // Get predictions for current word being typed
            val currentWord = _currentWord.value.lowercase()
            val predictions = predictiveEngine.getPredictions(currentWord)
            _suggestions.value = predictions.map { it.word }
        } else {
            // Get next word predictions after space
            val predictions = predictiveEngine.getNextWordPredictions(_inputBuffer.value)
            _suggestions.value = predictions.map { it.word }
        }
    }

    fun onTextInput(text: String) {
        viewModelScope.launch {
            if (text == " ") {
                onWordComplete()
                _inputBuffer.value += " "
            } else if (text == "\n") {
                onWordComplete()
            } else {
                _currentWord.value += text
                _inputBuffer.value += text
                _isMidWord.value = true
                updateSuggestions()
            }
        }
    }

    fun onBackspace() {
        //prob needs to be changed later
        viewModelScope.launch {
            if (_currentWord.value.isNotEmpty()) {
                val newWord = _currentWord.value.dropLast(1)
                _currentWord.value = newWord
                _inputBuffer.value = _inputBuffer.value.dropLast(1)

                _isMidWord.value = newWord.isNotEmpty()
                if (_isMidWord.value) {
                    val predictions = predictiveEngine.getPredictions(newWord)
                    _suggestions.value = predictions.map { it.word }
                } else {
                    _suggestions.value = emptyList()
                }
            }
        }
    }

    fun onSuggestionSelected(suggestion: String) {
        viewModelScope.launch {
            val oldWordLength = _currentWord.value.length
            _inputBuffer.value = _inputBuffer.value.dropLast(oldWordLength)
            _currentWord.value = ""
            _inputBuffer.value += suggestion
            _currentWord.value = ""
            _isMidWord.value = false
            updateSuggestions()
        }
    }

    private fun onWordComplete(word: String = _currentWord.value) {
        if (word.isNotEmpty()) {
            _currentWord.value = ""
            _isMidWord.value = false
            updateSuggestions()
        }
    }

    companion object {
        private const val MAX_SUGGESTIONS = 3
    }
}
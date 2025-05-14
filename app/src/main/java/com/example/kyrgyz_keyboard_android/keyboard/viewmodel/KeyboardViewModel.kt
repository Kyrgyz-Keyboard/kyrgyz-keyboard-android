package com.example.kyrgyz_keyboard_android.keyboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kyrgyz_keyboard_android.keyboard.predictive_text.PredictiveTextEngineImpl
import com.example.kyrgyz_keyboard_android.keyboard.model.CapsLockState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class KeyboardViewModel : ViewModel() {
    private val predictiveEngine = PredictiveTextEngineImpl()

    private val _keyboardState = MutableStateFlow(KeyboardState())
    val keyboardState: StateFlow<KeyboardState> = _keyboardState.asStateFlow()

    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions: StateFlow<List<String>> = _suggestions.asStateFlow()

    init {
        updateSuggestions()
    }

    fun toggleKeyboardMode() = viewModelScope.launch {
        _keyboardState.update { it.copy(isSymbolsMode = !it.isSymbolsMode) }
    }

    fun toggleSymbolsLayout() = viewModelScope.launch {
        _keyboardState.update { it.copy(isSymbolsLayout2 = !it.isSymbolsLayout2) }
    }

    fun toggleLatinLayout() {
        _keyboardState.update { it.copy(
            isLatinLayout = true,
            isEnesayLayout = false
        ) }
    }

    fun toggleCyrillicLayout() {
        _keyboardState.update { it.copy(
            isLatinLayout = false,
            isEnesayLayout = false
        ) }
    }

    fun toggleEnesayLayout() {
        _keyboardState.update { it.copy(
            isEnesayLayout = !it.isEnesayLayout,
            isLatinLayout = false
        ) }
    }

    fun toggleDarkMode() = viewModelScope.launch {
        _keyboardState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun updateCapsLockState(newState: CapsLockState) = viewModelScope.launch {
        _keyboardState.update { it.copy(capsLockState = newState) }
    }

    private fun updateSuggestions() {
        val currentState = _keyboardState.value
        val predictions = when {
            currentState.isMidWord -> predictiveEngine.getPredictions(currentState.currentWord.lowercase())
            else -> predictiveEngine.getNextWordPredictions(currentState.inputBuffer)
        }
        _suggestions.value = predictions.map { it.word }
    }

    fun onTextInput(text: String) = viewModelScope.launch {
        when (text) {
            " " -> handleSpace()
            "\n" -> handleNewline()
            else -> handleCharacterInput(text)
        }
    }

    fun onBackspace() = viewModelScope.launch {
        _keyboardState.update { state ->
            if (state.currentWord.isNotEmpty()) {
                state.copy(
                    currentWord = state.currentWord.dropLast(1),
                    inputBuffer = state.inputBuffer.dropLast(1),
                    isMidWord = state.currentWord.length > 1
                )
            } else state
        }
        updateSuggestions()
    }

    fun onSuggestionSelected(suggestion: String) = viewModelScope.launch {
        _keyboardState.update { state ->
            state.copy(
                inputBuffer = state.inputBuffer.dropLast(state.currentWord.length) + suggestion,
                currentWord = "",
                isMidWord = false
            )
        }
        updateSuggestions()
    }

    private fun handleSpace() {
        completeCurrentWord()
        _keyboardState.update { it.copy(inputBuffer = it.inputBuffer + " ") }
    }

    private fun handleNewline() {
        completeCurrentWord()
    }

    private fun handleCharacterInput(text: String) {
        _keyboardState.update { state ->
            state.copy(
                currentWord = state.currentWord + text,
                inputBuffer = state.inputBuffer + text,
                isMidWord = true
            )
        }
        updateSuggestions()
    }

    private fun completeCurrentWord() {
        _keyboardState.update { state ->
            if (state.currentWord.isNotEmpty()) {
                state.copy(currentWord = "", isMidWord = false)
            } else state
        }
        updateSuggestions()
    }

    companion object {
        private const val MAX_SUGGESTIONS = 3
    }
}

data class KeyboardState(
    val capsLockState: CapsLockState = CapsLockState.OFF,
    val currentWord: String = "",
    val inputBuffer: String = "",
    val isMidWord: Boolean = false,
    val isSymbolsMode: Boolean = false,
    val isSymbolsLayout2: Boolean = false,
    val isLatinLayout: Boolean = false,
    val isEnesayLayout: Boolean = false,
    val isDarkMode: Boolean = false
)
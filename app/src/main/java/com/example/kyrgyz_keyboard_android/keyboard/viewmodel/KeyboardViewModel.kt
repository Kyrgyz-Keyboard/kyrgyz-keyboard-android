package com.example.kyrgyz_keyboard_android.keyboard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.kyrgyz_keyboard_android.keyboard.model.CapsLockState
import com.example.kyrgyz_keyboard_android.keyboard.predictive_text.PredictiveTextEngineImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class KeyboardViewModel(application: Application) : AndroidViewModel(application) {
    private var predictiveEngine = PredictiveTextEngineImpl(application)
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

    fun toggleDictionaryMode() {
        _keyboardState.update { it.copy(isDictionaryMode = !it.isDictionaryMode) }
        updateSuggestions()
    }

    fun updateCapsLockState(newState: CapsLockState) = viewModelScope.launch {
        _keyboardState.update { it.copy(capsLockState = newState) }
    }

    private fun updateSuggestions() = viewModelScope.launch {
        try {
            if (_keyboardState.value.isDictionaryMode) {
                _suggestions.value = emptyList()
                return@launch
            }
            
            repeat(10) {
                if (predictiveEngine.isReady()) return@repeat
                kotlinx.coroutines.delay(50)
            }
    
            val currentState = _keyboardState.value
            val textToPredict = currentState.inputBuffer
            
            if (textToPredict.isEmpty()) {
                if (currentState.hadTextBefore) {
                    _suggestions.value = emptyList()
                } else {
                    _suggestions.value = listOf("мен", "сен", "биз", "силер", "бул")
                }
            } else {
                _suggestions.value = predictiveEngine.getPredictions(textToPredict.lowercase())
                
                if (!currentState.hadTextBefore) {
                    _keyboardState.update { it.copy(hadTextBefore = true) }
                }
            }
        } catch (_: OutOfMemoryError) {
            _suggestions.value = emptyList()
        }
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
                )
            } else state
        }
        updateSuggestions()
    }

    fun onSuggestionSelected(suggestion: String) = viewModelScope.launch {
        _keyboardState.update { state ->
            val newInputBuffer = if (state.currentWord.isEmpty()) {
                state.inputBuffer + suggestion
            } else {
                state.inputBuffer.dropLast(state.currentWord.length) + suggestion
            }
            
            state.copy(
                currentWord = "",
                inputBuffer = state.currentWord,
                capsLockState = CapsLockState.OFF,
                hadTextBefore = true,
                justSelectedSuggestion = true
            )
        }
        
        updateSuggestions()
    }

    fun resetSuggestionSelectionFlag() {
        _keyboardState.update { it.copy(justSelectedSuggestion = false) }
    }

    private fun handleSpace() {
        completeCurrentWord()
        _keyboardState.update { state ->
            val lastChar = state.inputBuffer.lastOrNull()
            val shouldCapitalize = lastChar == '.' || lastChar == '!' || lastChar == '?'
            
            if (shouldCapitalize) {
                updateCapsLockState(CapsLockState.TEMPORARY)
            }
            
            state.copy(inputBuffer = state.inputBuffer + " ")
        }
    }

    private fun handleNewline() {
        completeCurrentWord()
    }

    private fun handleCharacterInput(text: String) {
        _keyboardState.update { state ->
            state.copy(
                currentWord = state.currentWord + text,
                inputBuffer = state.inputBuffer + text,
            )
        }
        updateSuggestions()
    }

    private fun completeCurrentWord() {
        _keyboardState.update { state ->
            if (state.currentWord.isNotEmpty()) {
                state.copy(
                    currentWord = "",
                )
            } else state
        }
        updateSuggestions()
    }

    fun resetState() = viewModelScope.launch {
        _keyboardState.update { state ->
            state.copy(
                currentWord = "",
                inputBuffer = "",
                hadTextBefore = false
            )
        }
        
        _suggestions.value = emptyList()
        predictiveEngine.reset()
        updateSuggestions()
    }
}
data class KeyboardState(
    val capsLockState: CapsLockState = CapsLockState.TEMPORARY,
    val justSelectedSuggestion: Boolean = false,
    val isDictionaryMode: Boolean = false,
    val currentWord: String = "",
    val inputBuffer: String = "",
    val isSymbolsMode: Boolean = false,
    val isSymbolsLayout2: Boolean = false,
    val isLatinLayout: Boolean = false,
    val isEnesayLayout: Boolean = false,
    val isDarkMode: Boolean = false,
    val hadTextBefore: Boolean = false
)

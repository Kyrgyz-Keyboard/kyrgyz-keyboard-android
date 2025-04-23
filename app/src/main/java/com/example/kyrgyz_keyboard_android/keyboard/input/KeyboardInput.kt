package com.example.kyrgyz_keyboard_android.keyboard.input

import android.content.Context
import com.example.kyrgyz_keyboard_android.R
import com.example.kyrgyz_keyboard_android.keyboard.model.CapsLockState
import com.example.kyrgyz_keyboard_android.keyboard.model.KeyUiModel
import com.example.kyrgyz_keyboard_android.keyboard.service.KyrgyzKeyboardIME
import com.example.kyrgyz_keyboard_android.keyboard.viewmodel.KeyboardViewModel
import com.example.kyrgyz_keyboard_android.ui.keyboard.utils.KeyboardConstants

fun handleKeyClick(
    key: KeyUiModel,
    capsLockEnabled: CapsLockState,
    context: Context,
    viewModel: KeyboardViewModel
) {
    val inputConnection = (context as? KyrgyzKeyboardIME)?.currentInputConnection

    when {
        key.img == R.drawable.ic_remove -> {
            inputConnection?.deleteSurroundingText(1, 0)
            viewModel.onBackspace()
        }
        key.ch == KeyboardConstants.SPACE_CHARACTER -> {
            inputConnection?.commitText(" ", 1)
            viewModel.onTextInput(" ")
        }
        !key.isSpecial && key.ch != null -> {
            val text = when {
                capsLockEnabled == CapsLockState.TEMPORARY ||
                        capsLockEnabled == CapsLockState.LOCKED -> key.ch.uppercase()
                else -> key.ch.lowercase()
            }
            inputConnection?.commitText(text, 1)
            viewModel.onTextInput(text)

            if (capsLockEnabled == CapsLockState.TEMPORARY) {
                viewModel.updateCapsLockState(CapsLockState.OFF)
            }
        }
    }
}

fun handleSuggestionClick(
    suggestion: String,
    context: Context,
    viewModel: KeyboardViewModel
) {
    val inputConnection = (context as? KyrgyzKeyboardIME)?.currentInputConnection
    val currentWord = viewModel.currentWord.value

    if (currentWord.isNotEmpty()) {
        // Fix currentWord.length somehow (the issue is when using backspace)
        inputConnection?.deleteSurroundingText(currentWord.length, 0)
    }

    inputConnection?.commitText(suggestion, 1)
    inputConnection?.commitText(" ", 1)
    viewModel.onSuggestionSelected(suggestion)
}
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
    val inputConnection = (context as? KyrgyzKeyboardIME)?.currentInputConnection ?: return

    when {
        key.img == R.drawable.ic_remove -> {
            if (viewModel.keyboardState.value.isEnesayLayout) {
                inputConnection.deleteSurroundingText(2, 0)
            } else {
                inputConnection.deleteSurroundingText(1, 0)
            }
            viewModel.onBackspace()
        }
        key.img == R.drawable.ic_enter -> {
            inputConnection.commitText("\n", 1)
            viewModel.onTextInput("\n")
        }
        key.ch == KeyboardConstants.CYRILLIC_SPACE
                || key.ch == KeyboardConstants.LATIN_SPACE
                || key.ch == KeyboardConstants.ENESAY_SPACE -> {
            inputConnection.commitText(" ", 1)
            viewModel.onTextInput(" ")
        }
        !key.isSpecial && key.ch != null -> {
            val text = when (capsLockEnabled) {
                CapsLockState.TEMPORARY, CapsLockState.LOCKED -> key.ch.uppercase()
                else -> key.ch.lowercase()
            }
            inputConnection.commitText(text, 1)
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
    val inputConnection = (context as? KyrgyzKeyboardIME)?.currentInputConnection ?: return
    val keyboardState = viewModel.keyboardState.value

    if (keyboardState.currentWord.isNotEmpty()) {
        inputConnection.deleteSurroundingText(keyboardState.currentWord.length, 0)
    }

    inputConnection.commitText("$suggestion ", 1)
    viewModel.onSuggestionSelected(suggestion)
}
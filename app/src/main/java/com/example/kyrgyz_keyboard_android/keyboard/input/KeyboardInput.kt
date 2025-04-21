package com.example.kyrgyz_keyboard_android.keyboard.input

import android.content.Context
import com.example.kyrgyz_keyboard_android.R
import com.example.kyrgyz_keyboard_android.keyboard.model.CapsLockState
import com.example.kyrgyz_keyboard_android.keyboard.model.KeyUiModel
import com.example.kyrgyz_keyboard_android.keyboard.service.KyrgyzKeyboardIME
import com.example.kyrgyz_keyboard_android.keyboard.viewmodel.KeyboardViewModel
import com.example.kyrgyz_keyboard_android.ui.keyboard.utils.KeyboardConstants

fun handleKeyClick(
    key: KeyUiModel, capsLockEnabled: CapsLockState, context: Context, viewModel: KeyboardViewModel
) {
    val inputConnection = (context as? KyrgyzKeyboardIME)?.currentInputConnection

    when {
        key.img == R.drawable.ic_remove -> {
            inputConnection?.deleteSurroundingText(1, 0)
            viewModel.onBackspace()
        }

        key.ch != null -> {
            if (key.ch == KeyboardConstants.SPACE_CHARACTER) {
                viewModel.onWordComplete()
                inputConnection?.commitText(" ", 1)
            } else {
                val text = when {
                    capsLockEnabled == CapsLockState.TEMPORARY || capsLockEnabled == CapsLockState.LOCKED -> key.ch.uppercase()
                    else -> key.ch.lowercase()
                }
                inputConnection?.commitText(text, text.length)
                viewModel.onTextInput(text)

                if (capsLockEnabled == CapsLockState.TEMPORARY) {
                    viewModel.updateCapsLockState(CapsLockState.OFF)
                }
            }
        }
    }
}
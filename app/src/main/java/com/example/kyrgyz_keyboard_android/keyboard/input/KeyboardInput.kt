package com.example.kyrgyz_keyboard_android.keyboard.input

import android.content.Context
import com.example.kyrgyz_keyboard_android.R
import com.example.kyrgyz_keyboard_android.keyboard.model.CapsLockState
import com.example.kyrgyz_keyboard_android.keyboard.model.KeyUiModel
import com.example.kyrgyz_keyboard_android.keyboard.service.KyrgyzKeyboardIME

fun handleKeyClick(
    key: KeyUiModel,
    capsLockEnabled: CapsLockState,
    context: Context,
    onCapsLockChanged: (CapsLockState) -> Unit
) {
    val inputConnection = (context as? KyrgyzKeyboardIME)?.currentInputConnection

    when {
        key.img == R.drawable.ic_remove -> {
            inputConnection?.deleteSurroundingText(1, 0)
        }

        key.ch == "аралык" -> {
            inputConnection?.commitText(" ", 1)
        }

        !key.isSpecial && key.ch != null -> {
            val textToCommit =
                if (capsLockEnabled == CapsLockState.TEMPORARY || capsLockEnabled == CapsLockState.LOCKED) key.ch.uppercase()
                else key.ch.lowercase()
            inputConnection?.commitText(textToCommit, textToCommit.length)
            if (capsLockEnabled == CapsLockState.TEMPORARY) {
                onCapsLockChanged(CapsLockState.OFF)
            }
        }
    }
}
package com.example.kyrgyz_keyboard_android.ui.keyboard.components

import androidx.compose.runtime.Composable
import com.example.kyrgyz_keyboard_android.R
import com.example.kyrgyz_keyboard_android.keyboard.model.CapsLockState
import com.example.kyrgyz_keyboard_android.keyboard.model.KeyUiModel

@Composable
fun CapsLockRow(
    keys: List<KeyUiModel>,
    capsLockEnabled: CapsLockState,
    onCapsLockChanged: (CapsLockState) -> Unit
) {
    KeyboardRow(
        keys = keys.map { key ->
            if (key.img == R.drawable.ic_caps) {
                key.copy(isActive = capsLockEnabled)
            } else key
        }, capsLockEnabled = capsLockEnabled, onCapsLockChanged = onCapsLockChanged
    ) { key ->
        if (key.img == R.drawable.ic_caps) {
            when (capsLockEnabled) {
                CapsLockState.TEMPORARY -> onCapsLockChanged(CapsLockState.OFF)
                CapsLockState.LOCKED -> onCapsLockChanged(CapsLockState.OFF)
                CapsLockState.OFF -> onCapsLockChanged(CapsLockState.TEMPORARY)
            }
        }
    }
}
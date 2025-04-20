package com.example.kyrgyz_keyboard_android.ui.keyboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.kyrgyz_keyboard_android.keyboard.model.CapsLockState
import com.example.kyrgyz_keyboard_android.keyboard.model.KeyUiModel
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions.keyHeight
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions.keySpacing
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions.keyVerticalPadding

@Composable
fun KeyboardRow(
    keys: List<KeyUiModel>,
    capsLockEnabled: CapsLockState,
    onCapsLockChanged: (CapsLockState) -> Unit,
    onKeyClick: (KeyUiModel) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = keyVerticalPadding),
        horizontalArrangement = Arrangement.spacedBy(keySpacing)
    ) {
        keys.forEach { key ->
            KeyButton(
                modifier = Modifier
                    .weight(key.weight)
                    .height(keyHeight),
                key = key,
                capsLockEnabled = capsLockEnabled,
                onClick = onKeyClick,
                onCapsLockChanged = onCapsLockChanged
            )
        }
    }
}
package com.example.kyrgyz_keyboard_android.ui.keyboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.kyrgyz_keyboard_android.R
import com.example.kyrgyz_keyboard_android.keyboard.model.CapsLockState
import com.example.kyrgyz_keyboard_android.keyboard.model.KeyUiModel
import com.example.kyrgyz_keyboard_android.keyboard.viewmodel.KeyboardViewModel
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions

@Composable
fun CapsLockRow(
    keys: List<KeyUiModel>, capsLockEnabled: CapsLockState, viewModel: KeyboardViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimensions.keyVerticalPadding),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.keySpacing)
    ) {
        keys.forEach { key ->
            val modifiedKey = when (key.img) {
                R.drawable.ic_caps -> key.copy(isActive = capsLockEnabled)
                else -> key
            }

            KeyButton(
                modifier = Modifier
                    .weight(key.weight)
                    .height(Dimensions.keyHeight),
                key = modifiedKey,
                capsLockEnabled = capsLockEnabled,
                viewModel = viewModel
            )
        }
    }
}
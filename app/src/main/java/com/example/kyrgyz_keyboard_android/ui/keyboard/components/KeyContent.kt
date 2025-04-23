package com.example.kyrgyz_keyboard_android.ui.keyboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.example.kyrgyz_keyboard_android.R
import com.example.kyrgyz_keyboard_android.keyboard.model.CapsLockState
import com.example.kyrgyz_keyboard_android.keyboard.model.KeyUiModel
import com.example.kyrgyz_keyboard_android.keyboard.viewmodel.KeyboardViewModel
import com.example.kyrgyz_keyboard_android.ui.keyboard.utils.KeyboardConstants
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions.keyIconSize
import com.example.kyrgyz_keyboard_android.ui.theme.EnterImgColor
import com.example.kyrgyz_keyboard_android.ui.theme.KeyTextColor
import com.example.kyrgyz_keyboard_android.ui.theme.keyboardTextStyle

@Composable
fun KeyContent(
    key: KeyUiModel, capsLockEnabled: CapsLockState, viewModel: KeyboardViewModel
) {
    when {
        key.img != null -> {
            Image(
                painter = painterResource(id = key.img),
                contentDescription = null,
                modifier = Modifier.size(keyIconSize),
                colorFilter = if (key.img == R.drawable.ic_enter) {
                    ColorFilter.tint(EnterImgColor)
                } else {
                    ColorFilter.tint(KeyTextColor)
                }
            )
        }

        else -> {
            val text = when {
                key.ch == KeyboardConstants.SPACE_CHARACTER -> key.ch
                capsLockEnabled == CapsLockState.TEMPORARY || capsLockEnabled == CapsLockState.LOCKED -> key.ch?.uppercase()
                else -> key.ch?.lowercase()
            } ?: ""

            Text(
                text = text, style = keyboardTextStyle, color = KeyTextColor
            )
        }
    }
}
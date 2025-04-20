package com.example.kyrgyz_keyboard_android.ui.keyboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.kyrgyz_keyboard_android.keyboard.model.CapsLockState
import com.example.kyrgyz_keyboard_android.keyboard.model.KeyUiModel
import com.example.kyrgyz_keyboard_android.ui.keyboard.utils.KeyboardConstants.SPACE_CHARACTER
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions.keyContentPadding
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions.keyIconSize
import com.example.kyrgyz_keyboard_android.ui.theme.keyboardTextStyle

@Composable
fun KeyContent(
    key: KeyUiModel, capsLockEnabled: CapsLockState
) {
    when {
        !key.isSpecial -> {
            val displayChar = when {
                key.ch != null && (capsLockEnabled == CapsLockState.TEMPORARY || capsLockEnabled == CapsLockState.LOCKED) && key.ch != SPACE_CHARACTER -> key.ch.uppercase()
                else -> key.ch
            }
            Text(
                text = displayChar ?: "",
                style = keyboardTextStyle,
                modifier = Modifier.padding(keyContentPadding)
            )
        }

        key.img != null -> {
            Image(
                painter = painterResource(id = key.img),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(keyIconSize)
            )
        }
    }
}
package com.example.kyrgyz_keyboard_android.ui.keyboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kyrgyz_keyboard_android.R
import com.example.kyrgyz_keyboard_android.keyboard.model.CapsLockState
import com.example.kyrgyz_keyboard_android.keyboard.model.KeyUiModel
import com.example.kyrgyz_keyboard_android.keyboard.viewmodel.KeyboardViewModel
import com.example.kyrgyz_keyboard_android.ui.keyboard.utils.KeyboardConstants
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions
import com.example.kyrgyz_keyboard_android.ui.theme.EnterImgColor
import com.example.kyrgyz_keyboard_android.ui.theme.KeyTextColor
import com.example.kyrgyz_keyboard_android.ui.theme.keyboardTextStyle
import com.example.kyrgyz_keyboard_android.ui.theme.symbolsBtnTextStyle

@Composable
fun KeyContent(
    key: KeyUiModel, capsLockEnabled: CapsLockState, viewModel: KeyboardViewModel
) {
    Box(contentAlignment = Alignment.Center) {
        when {
            key.img != null -> {
                Image(
                    painter = painterResource(id = key.img),
                    contentDescription = null,
                    modifier = Modifier.size(Dimensions.keyIconSize),
                    colorFilter = when {
                        key.img == R.drawable.ic_enter -> ColorFilter.tint(EnterImgColor)
                        key.img == R.drawable.ic_emoji -> null
                        else -> ColorFilter.tint(KeyTextColor)
                    }
                )
            }
            else -> {
                val text = when {
                    key.ch == KeyboardConstants.CYRILLIC_SPACE
                            || key.ch == KeyboardConstants.LATIN_SPACE
                            || key.ch == KeyboardConstants.ENESAY_SPACE -> key.ch
                    key.isSpecial && key.ch != null -> key.ch
                    capsLockEnabled == CapsLockState.TEMPORARY ||
                            capsLockEnabled == CapsLockState.LOCKED -> key.ch?.uppercase()
                    else -> key.ch?.lowercase()
                } ?: ""

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .defaultMinSize(minWidth = 32.dp)  // Minimum size for small keys
                        .height(45.dp)
                ) {
                    if (key.isSpecial || key.ch == KeyboardConstants.CYRILLIC_SPACE
                        || key.ch == KeyboardConstants.LATIN_SPACE
                        || key.ch == KeyboardConstants.ENESAY_SPACE
                    ) {
                        Text(
                            text = text,
                            style = symbolsBtnTextStyle,
                            color = KeyTextColor
                        )
                    } else {
                        Text(
                            text = text,
                            style = keyboardTextStyle,
                            color = KeyTextColor
                        )
                    }

                    key.hint?.let { hint ->
                        Text(
                            text = hint,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 10.sp
                            ),
                            color = KeyTextColor.copy(alpha = 0.5f),
                            modifier = Modifier.align(Alignment.TopEnd)
                                .padding(end = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
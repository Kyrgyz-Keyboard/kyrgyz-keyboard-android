package com.example.kyrgyz_keyboard_android.ui.keyboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import com.example.kyrgyz_keyboard_android.R
import com.example.kyrgyz_keyboard_android.keyboard.input.handleKeyClick
import com.example.kyrgyz_keyboard_android.keyboard.model.CapsLockState
import com.example.kyrgyz_keyboard_android.keyboard.model.KeyUiModel
import com.example.kyrgyz_keyboard_android.keyboard.viewmodel.KeyboardViewModel
import com.example.kyrgyz_keyboard_android.ui.keyboard.utils.KeyboardConstants
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions
import com.example.kyrgyz_keyboard_android.ui.theme.EnterColor
import com.example.kyrgyz_keyboard_android.ui.theme.KeyBackgroundColor
import com.example.kyrgyz_keyboard_android.ui.theme.KeyGray
import com.example.kyrgyz_keyboard_android.ui.theme.KeyLockedBackgroundColor
import kotlinx.coroutines.delay

@Composable
fun KeyButton(
    modifier: Modifier = Modifier,
    key: KeyUiModel,
    capsLockEnabled: CapsLockState,
    viewModel: KeyboardViewModel
) {
    val context = LocalContext.current
    var isBackspacePressed by remember { mutableStateOf(false) }

    LaunchedEffect(isBackspacePressed) {
        if (isBackspacePressed && key.img == R.drawable.ic_remove) {
            var iterations = 0
            var currentSpeed = KeyboardConstants.INITIAL_DELETE_SPEED

            handleKeyClick(key, capsLockEnabled, context, viewModel)

            while (isBackspacePressed) {
                delay(currentSpeed)
                handleKeyClick(key, capsLockEnabled, context, viewModel)
                iterations++

                currentSpeed = if (iterations < KeyboardConstants.INITIAL_SPEED_PHASE_ITERATIONS) {
                    (currentSpeed * KeyboardConstants.SPEED_DECREASE_FACTOR_INITIAL).toLong()
                        .coerceAtLeast(KeyboardConstants.MIN_DELETE_SPEED)
                } else {
                    (currentSpeed * KeyboardConstants.SPEED_DECREASE_FACTOR_NORMAL).toLong()
                        .coerceAtLeast(KeyboardConstants.MIN_DELETE_SPEED)
                }
            }
        }
    }

    Box(
        modifier = modifier
            .background(
                color = getBackgroundColor(key),
                shape = RoundedCornerShape(Dimensions.keyCornerRadius)
            )
            .pointerInput(key, capsLockEnabled) {
                detectTapGestures(
                    onPress = {
                        when {
                            key.img == R.drawable.ic_remove -> {
                                isBackspacePressed = true
                                tryAwaitRelease()
                                isBackspacePressed = false
                            }
                            key.ch == "=\\<" || key.ch == "?123" -> viewModel.toggleSymbolsLayout()
                            key.ch == KeyboardConstants.SYMBOLS_CHARACTER -> viewModel.toggleKeyboardMode()
                            key.ch == KeyboardConstants.ALPHA_CHARACTER -> viewModel.toggleKeyboardMode()
                            else -> handleKeyClick(key, capsLockEnabled, context, viewModel)
                        }
                    },
                    onTap = {
                        if (key.img == R.drawable.ic_caps) {
                            viewModel.updateCapsLockState(
                                if (capsLockEnabled == CapsLockState.OFF) CapsLockState.TEMPORARY
                                else CapsLockState.OFF
                            )
                        }
                    },
                    onDoubleTap = {
                        if (key.img == R.drawable.ic_caps) {
                            viewModel.updateCapsLockState(
                                if (capsLockEnabled != CapsLockState.LOCKED) CapsLockState.LOCKED
                                else CapsLockState.OFF
                            )
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        KeyContent(key = key, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
    }
}

private fun getBackgroundColor(key: KeyUiModel): Color = when {
    key.isActive == CapsLockState.LOCKED -> KeyLockedBackgroundColor
    key.isSpecial && key.img != R.drawable.ic_enter -> KeyGray
    key.isSpecial && key.img == R.drawable.ic_enter -> EnterColor
    else -> KeyBackgroundColor
}
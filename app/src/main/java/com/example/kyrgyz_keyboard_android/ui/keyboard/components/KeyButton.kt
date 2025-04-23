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
import com.example.kyrgyz_keyboard_android.ui.keyboard.utils.KeyboardConstants.INITIAL_DELETE_SPEED
import com.example.kyrgyz_keyboard_android.ui.keyboard.utils.KeyboardConstants.INITIAL_SPEED_PHASE_ITERATIONS
import com.example.kyrgyz_keyboard_android.ui.keyboard.utils.KeyboardConstants.MIN_DELETE_SPEED
import com.example.kyrgyz_keyboard_android.ui.keyboard.utils.KeyboardConstants.SPEED_DECREASE_FACTOR_INITIAL
import com.example.kyrgyz_keyboard_android.ui.keyboard.utils.KeyboardConstants.SPEED_DECREASE_FACTOR_NORMAL
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions.keyCornerRadius
import com.example.kyrgyz_keyboard_android.ui.theme.EnterBlue
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
            var currentSpeed = INITIAL_DELETE_SPEED

            handleKeyClick(key, capsLockEnabled, context, viewModel)
            viewModel.onBackspace()

            while (isBackspacePressed) {
                delay(currentSpeed)
                handleKeyClick(key, capsLockEnabled, context, viewModel)
                viewModel.onBackspace()
                iterations++

                currentSpeed = if (iterations < INITIAL_SPEED_PHASE_ITERATIONS) {
                    (currentSpeed * SPEED_DECREASE_FACTOR_INITIAL).toLong()
                        .coerceAtLeast(MIN_DELETE_SPEED)
                } else {
                    (currentSpeed * SPEED_DECREASE_FACTOR_NORMAL).toLong()
                        .coerceAtLeast(MIN_DELETE_SPEED)
                }
            }
        }
    }

    Box(
        modifier = modifier
            .background(
                color = getBackgroundColor(key), shape = RoundedCornerShape(keyCornerRadius)
            )
            .pointerInput(key, capsLockEnabled) {
                detectTapGestures(onPress = {
                    if (key.img == R.drawable.ic_remove) {
                        isBackspacePressed = true
                        tryAwaitRelease()
                        isBackspacePressed = false
                    } else {
                        handleKeyClick(key, capsLockEnabled, context, viewModel)
                    }
                }, onTap = {
                    if (key.img == R.drawable.ic_caps) {
                        when (capsLockEnabled) {
                            CapsLockState.OFF -> viewModel.updateCapsLockState(CapsLockState.TEMPORARY)
                            else -> viewModel.updateCapsLockState(CapsLockState.OFF)
                        }
                    }
                }, onDoubleTap = {
                    if (key.img == R.drawable.ic_caps) {
                        viewModel.updateCapsLockState(CapsLockState.LOCKED)
                    }
                })
            }, contentAlignment = Alignment.Center
    ) {
        KeyContent(key = key, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
    }
}

private fun getBackgroundColor(key: KeyUiModel): Color = when {
    key.isActive == CapsLockState.LOCKED -> KeyLockedBackgroundColor
    key.isSpecial && key.img != R.drawable.ic_enter -> KeyGray
    key.isSpecial && key.img == R.drawable.ic_enter -> EnterBlue
    else -> KeyBackgroundColor
}
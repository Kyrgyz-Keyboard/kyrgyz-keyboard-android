package com.example.kyrgyz_keyboard_android.ui.keyboard.components

import android.content.Context
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
import com.example.kyrgyz_keyboard_android.ui.keyboard.utils.KeyboardConstants.INITIAL_DELETE_SPEED
import com.example.kyrgyz_keyboard_android.ui.keyboard.utils.KeyboardConstants.INITIAL_SPEED_PHASE_ITERATIONS
import com.example.kyrgyz_keyboard_android.ui.keyboard.utils.KeyboardConstants.MIN_DELETE_SPEED
import com.example.kyrgyz_keyboard_android.ui.keyboard.utils.KeyboardConstants.SPEED_DECREASE_FACTOR_INITIAL
import com.example.kyrgyz_keyboard_android.ui.keyboard.utils.KeyboardConstants.SPEED_DECREASE_FACTOR_NORMAL
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions.keyCornerRadius
import com.example.kyrgyz_keyboard_android.ui.theme.KeyBackgroundColor
import com.example.kyrgyz_keyboard_android.ui.theme.KeyGray
import com.example.kyrgyz_keyboard_android.ui.theme.KeyLockedBackgroundColor
import kotlinx.coroutines.delay

@Composable
fun KeyButton(
    modifier: Modifier = Modifier,
    key: KeyUiModel,
    capsLockEnabled: CapsLockState,
    onClick: (KeyUiModel) -> Unit,
    onCapsLockChanged: (CapsLockState) -> Unit
) {
    val context = LocalContext.current
    var isBackspacePressed by remember { mutableStateOf(false) }

    LaunchedEffect(isBackspacePressed) {
        if (isBackspacePressed && key.img == R.drawable.ic_remove) {
            var iterations = 0
            var currentSpeed = INITIAL_DELETE_SPEED

            handleKeyClick(key, capsLockEnabled, context, onCapsLockChanged)

            while (isBackspacePressed) {
                delay(currentSpeed)
                handleKeyClick(key, capsLockEnabled, context, onCapsLockChanged)
                iterations++

                currentSpeed = if (iterations < INITIAL_SPEED_PHASE_ITERATIONS) {
                    (currentSpeed * SPEED_DECREASE_FACTOR_INITIAL).toLong().coerceAtLeast(MIN_DELETE_SPEED)
                } else {
                    (currentSpeed * SPEED_DECREASE_FACTOR_NORMAL).toLong().coerceAtLeast(MIN_DELETE_SPEED)
                }
            }
        }
    }

    Box(
        modifier = modifier
            .background(
                color = getBackgroundColor(key),
                shape = RoundedCornerShape(keyCornerRadius)
            )
            .pointerInput(key, capsLockEnabled) {
                detectTapGestures(
                    onPress = {
                        if (key.img == R.drawable.ic_remove) {
                            isBackspacePressed = true
                            tryAwaitRelease()
                            isBackspacePressed = false
                        } else {
                            handleKeyClick(key, capsLockEnabled, context, onCapsLockChanged)
                        }
                    },
                    onTap = {
                        if (key.img == R.drawable.ic_caps || key.isSpecial) {
                            handleTap(key, capsLockEnabled, context, onClick, onCapsLockChanged)
                        }
                    },
                    onLongPress = {
                        handleLongPress(key, onCapsLockChanged) {
                            if (key.img == R.drawable.ic_remove) {
                                isBackspacePressed = true
                            }
                        }
                    },
                    onDoubleTap = { handleDoubleTap(key, capsLockEnabled, onCapsLockChanged) }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        KeyContent(key = key, capsLockEnabled = capsLockEnabled)
    }
}

private fun getBackgroundColor(key: KeyUiModel): Color = when {
    key.isActive == CapsLockState.LOCKED -> KeyLockedBackgroundColor
    key.isSpecial -> KeyGray
    else -> KeyBackgroundColor
}

private fun handleTap(
    key: KeyUiModel,
    capsLockEnabled: CapsLockState,
    context: Context,
    onClick: (KeyUiModel) -> Unit,
    onCapsLockChanged: (CapsLockState) -> Unit
) {
    onClick(key)
    handleKeyClick(key, capsLockEnabled, context, onCapsLockChanged)
}

private fun handleLongPress(
    key: KeyUiModel, onCapsLockChanged: (CapsLockState) -> Unit, onBackspaceStart: () -> Unit
) {
    when (key.img) {
        R.drawable.ic_remove -> onBackspaceStart()
        R.drawable.ic_caps -> onCapsLockChanged(CapsLockState.LOCKED)
    }
}

private fun handleDoubleTap(
    key: KeyUiModel, capsLockEnabled: CapsLockState, onCapsLockChanged: (CapsLockState) -> Unit
) {
    if (key.img == R.drawable.ic_caps) {
        when {
            capsLockEnabled == CapsLockState.TEMPORARY || capsLockEnabled == CapsLockState.OFF -> onCapsLockChanged(
                CapsLockState.LOCKED
            )

            else -> onCapsLockChanged(CapsLockState.OFF)
        }
    }
}
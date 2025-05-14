package com.example.kyrgyz_keyboard_android.ui.keyboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import com.example.kyrgyz_keyboard_android.ui.theme.EnterColorDark
import com.example.kyrgyz_keyboard_android.ui.theme.KeyBackgroundColor
import com.example.kyrgyz_keyboard_android.ui.theme.KeyBackgroundColorDark
import com.example.kyrgyz_keyboard_android.ui.theme.KeyGray
import com.example.kyrgyz_keyboard_android.ui.theme.KeyGrayDark
import com.example.kyrgyz_keyboard_android.ui.theme.KeyLockedBackgroundColor
import com.example.kyrgyz_keyboard_android.ui.theme.KeyLockedBackgroundColorDark
import kotlinx.coroutines.delay

@Composable
fun KeyButton(
    modifier: Modifier = Modifier,
    key: KeyUiModel,
    capsLockEnabled: CapsLockState,
    viewModel: KeyboardViewModel
) {
    val keyboardState by viewModel.keyboardState.collectAsState()
    val context = LocalContext.current
    var isBackspacePressed by remember { mutableStateOf(false) }
    var firstTapTime by remember { mutableLongStateOf(0L) }
    var waitingForSecondTap by remember { mutableStateOf(false) }

    val transformedKey = remember(key, keyboardState.isDarkMode) {
        if (key.ch == KeyboardConstants.LIGHT || key.ch == KeyboardConstants.DARK) {
            key.copy(ch = if (keyboardState.isDarkMode) KeyboardConstants.DARK else KeyboardConstants.LIGHT)
        } else key
    }

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
                color = getBackgroundColor(key, keyboardState.isDarkMode),
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
                            key.img == R.drawable.ic_globe -> {
                                when {
                                    viewModel.keyboardState.value.isEnesayLayout -> viewModel.toggleLatinLayout()
                                    viewModel.keyboardState.value.isLatinLayout -> viewModel.toggleCyrillicLayout()
                                    else -> viewModel.toggleLatinLayout()
                                }
                            }
                            transformedKey.ch == KeyboardConstants.DARK
                                    || transformedKey.ch == KeyboardConstants.LIGHT -> {
                                viewModel.toggleDarkMode()
                            }
                            key.ch == "=\\<" || key.ch == "?123" -> viewModel.toggleSymbolsLayout()
                            key.ch == KeyboardConstants.SYMBOLS_CHARACTER -> viewModel.toggleKeyboardMode()
                            key.ch == KeyboardConstants.ALPHA_CHARACTER -> viewModel.toggleKeyboardMode()
                            key.ch == KeyboardConstants.ENESAY_CHARACTER -> viewModel.toggleEnesayLayout()
                            else -> handleKeyClick(key, capsLockEnabled, context, viewModel)
                        }
                    },
                    onTap = {
                        if (key.img == R.drawable.ic_caps) {
                            val currentTime = System.currentTimeMillis()
                            if (waitingForSecondTap && currentTime - firstTapTime < 300) {
                                // Double tap detected
                                viewModel.updateCapsLockState(CapsLockState.LOCKED)
                                waitingForSecondTap = false
                            } else {
                                // First tap
                                firstTapTime = currentTime
                                waitingForSecondTap = true
                                viewModel.updateCapsLockState(
                                    if (capsLockEnabled == CapsLockState.OFF) CapsLockState.TEMPORARY
                                    else CapsLockState.OFF
                                )
                            }
                        }
                    },
//                    onDoubleTap = {
//                        if (key.img == R.drawable.ic_caps) {
//                            viewModel.updateCapsLockState(
//                                if (capsLockEnabled != CapsLockState.LOCKED) CapsLockState.LOCKED
//                                else CapsLockState.OFF
//                            )
//                        }
//                    },
                    onLongPress = {
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
        KeyContent(key = transformedKey, capsLockEnabled = capsLockEnabled, isDarkMode = keyboardState.isDarkMode, viewModel = viewModel)
    }
}

private fun getBackgroundColor(key: KeyUiModel, isDarkMode: Boolean): Color = when {
    key.isActive == CapsLockState.LOCKED ->
        if (isDarkMode) KeyLockedBackgroundColorDark else KeyLockedBackgroundColor
    key.isSpecial && key.img != R.drawable.ic_enter ->
        if (isDarkMode) KeyGrayDark else KeyGray
    key.isSpecial && key.img == R.drawable.ic_enter ->
        if (isDarkMode) EnterColorDark else EnterColor
    else ->
        if (isDarkMode) KeyBackgroundColorDark else KeyBackgroundColor
}
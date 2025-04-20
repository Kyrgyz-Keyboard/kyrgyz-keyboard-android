package com.example.kyrgyz_keyboard_android.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kyrgyz_keyboard_android.R
import com.example.kyrgyz_keyboard_android.keyboard.KeyUiModel
import com.example.kyrgyz_keyboard_android.keyboard.KeyboardLayout
import com.example.kyrgyz_keyboard_android.keyboard.KyrgyzKeyboardIME
import com.example.kyrgyz_keyboard_android.ui.theme.KeyGray
import com.example.kyrgyz_keyboard_android.ui.theme.KeyboardGray

enum class CapsLockState { OFF, TEMPORARY, LOCKED }

@Composable
fun HomeScreen() {
    var capsLockEnabled by remember { mutableStateOf(CapsLockState.TEMPORARY) }
    KeyboardLayout(capsLockEnabled = capsLockEnabled) { newState ->
        capsLockEnabled = newState
    }
}

@Composable
private fun KeyboardLayout(
    capsLockEnabled: CapsLockState, onCapsLockChanged: (CapsLockState) -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = KeyboardGray)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        with(KeyboardLayout) {
            KeyboardRow(keys = row2, capsLockEnabled, onCapsLockChanged)
            KeyboardRow(keys = row3, capsLockEnabled, onCapsLockChanged)
            CapsLockRow(
                keys = row4,
                capsLockEnabled = capsLockEnabled,
                onCapsLockChanged = onCapsLockChanged
            )
            KeyboardRow(keys = row5, capsLockEnabled, onCapsLockChanged)
        }
    }
}

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
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        keys.forEach { key ->
            KeyButton(
                modifier = Modifier
                    .weight(key.weight)
                    .height(48.dp),
                key = key,
                capsLockEnabled = capsLockEnabled,
                onClick = onKeyClick,
                onCapsLockChanged = onCapsLockChanged
            )
        }
    }
}

@Composable
private fun CapsLockRow(
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
                CapsLockState.TEMPORARY -> onCapsLockChanged(CapsLockState.LOCKED)
                CapsLockState.LOCKED -> onCapsLockChanged(CapsLockState.OFF)
                CapsLockState.OFF -> onCapsLockChanged(CapsLockState.TEMPORARY)
            }
        }
    }
}

@Composable
private fun KeyButton(
    modifier: Modifier = Modifier,
    key: KeyUiModel,
    capsLockEnabled: CapsLockState,
    onClick: (KeyUiModel) -> Unit,
    onCapsLockChanged: (CapsLockState) -> Unit
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .background(
                color = when {
                    key.isActive == CapsLockState.LOCKED -> Color.LightGray
                    key.isSpecial -> KeyGray
                    else -> Color.White
                }, shape = RoundedCornerShape(4.dp)
            )
            .clickable(
                interactionSource = interactionSource, indication = null
            ) {
                onClick(key)
                handleKeyClick(key, capsLockEnabled, context, onCapsLockChanged)
            }, contentAlignment = Alignment.Center
    ) {
        KeyContent(key = key, capsLockEnabled = capsLockEnabled)
    }
}

@Composable
private fun KeyContent(
    key: KeyUiModel, capsLockEnabled: CapsLockState
) {
    when {
        !key.isSpecial -> {
            val displayChar = when {
                key.ch != null && (capsLockEnabled == CapsLockState.TEMPORARY || capsLockEnabled == CapsLockState.LOCKED) && key.ch != "аралык" -> key.ch.uppercase()
                else -> key.ch
            }
            Text(
                text = displayChar ?: "",
                fontSize = 18.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(5.dp)
            )
        }

        key.img != null -> {
            Image(
                painter = painterResource(id = key.img),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

private fun handleKeyClick(
    key: KeyUiModel,
    capsLockEnabled: CapsLockState,
    context: Context,
    onCapsLockChanged: (CapsLockState) -> Unit
) {
    val inputConnection = (context as? KyrgyzKeyboardIME)?.currentInputConnection

    when {
        key.img == R.drawable.ic_remove -> {
            inputConnection?.deleteSurroundingText(1, 0)
        }

        key.ch == "аралык" -> {
            inputConnection?.commitText(" ", 1)
        }

        !key.isSpecial && key.ch != null -> {
            val textToCommit =
                if (capsLockEnabled == CapsLockState.TEMPORARY || capsLockEnabled == CapsLockState.LOCKED) key.ch.uppercase()
                else key.ch
            inputConnection?.commitText(textToCommit, textToCommit.length)
            if (capsLockEnabled == CapsLockState.TEMPORARY) {
                onCapsLockChanged(CapsLockState.OFF)
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
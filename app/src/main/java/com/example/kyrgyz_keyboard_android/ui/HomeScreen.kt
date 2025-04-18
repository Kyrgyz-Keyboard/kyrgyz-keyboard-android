package com.example.kyrgyz_keyboard_android.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.kyrgyz_keyboard_android.R
import com.example.kyrgyz_keyboard_android.keyboard.KeyUiModel
import com.example.kyrgyz_keyboard_android.keyboard.KeyboardLayout
import com.example.kyrgyz_keyboard_android.keyboard.KyrgyzKeyboardIME
import com.example.kyrgyz_keyboard_android.ui.theme.KeyGray
import com.example.kyrgyz_keyboard_android.ui.theme.KeyboardGray

//enum class CapsLockState { OFF, TEMPORARY, LOCKED }

@Composable
fun HomeScreen() {
    var capsLockEnabled by remember { mutableStateOf(false) }
    KeyboardLayout(capsLockEnabled = capsLockEnabled) { newState ->
        capsLockEnabled = newState
    }
}

@Composable
private fun KeyboardLayout(
    capsLockEnabled: Boolean,
    onCapsLockChanged: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = KeyboardGray)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        with(KeyboardLayout) {
            KeyboardRow(keys = row2, capsLockEnabled)
            KeyboardRow(keys = row3, capsLockEnabled)
            CapsLockRow(
                keys = row4,
                capsLockEnabled = capsLockEnabled,
                onCapsLockChanged = onCapsLockChanged
            )
            KeyboardRow(keys = row5, capsLockEnabled)
        }
    }
}

@Composable
fun KeyboardRow(keys: List<KeyUiModel>, capsLockEnabled: Boolean,
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
                onClick = onKeyClick
            )
        }
    }
}

@Composable
private fun CapsLockRow(
    keys: List<KeyUiModel>,
    capsLockEnabled: Boolean,
    onCapsLockChanged: (Boolean) -> Unit
) {
    KeyboardRow(
        keys = keys.map { key ->
            if (key.img == R.drawable.ic_caps) {
                key.copy(isActive = capsLockEnabled)
            } else key
        },
        capsLockEnabled = capsLockEnabled
    ) { key ->
        if (key.img == R.drawable.ic_caps) {
            onCapsLockChanged(!capsLockEnabled)
        }
    }
}

@Composable
private fun KeyButton(
    modifier: Modifier = Modifier,
    key: KeyUiModel,
    capsLockEnabled: Boolean,
    onClick: (KeyUiModel) -> Unit
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .background(
                color = when {
                    key.isActive -> Color.LightGray
                    key.isSpecial -> KeyGray
                    else -> Color.White
                },
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick(key)
                handleKeyClick(key, capsLockEnabled, context)
            },
        contentAlignment = Alignment.Center
    ) {
        KeyContent(key = key, capsLockEnabled = capsLockEnabled)
    }
}

@Composable
private fun KeyContent(key: KeyUiModel, capsLockEnabled: Boolean) {
    when {
        !key.isSpecial -> {
            val displayChar = when {
                key.ch != null && capsLockEnabled && key.ch != "аралык" -> key.ch.uppercase()
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

private fun handleKeyClick(key: KeyUiModel, capsLockEnabled: Boolean, context: Context) {
    val inputConnection = (context as? KyrgyzKeyboardIME)?.currentInputConnection

    when {
        key.img == R.drawable.ic_remove -> {
            inputConnection?.deleteSurroundingText(1, 0)
        }
        key.ch == "аралык" -> {
            inputConnection?.commitText(" ", 1)
        }
        !key.isSpecial && key.ch != null -> {
            val textToCommit = if (capsLockEnabled) {
                key.ch.uppercase()
            } else {
                key.ch
            }
            inputConnection?.commitText(textToCommit, textToCommit.length)
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
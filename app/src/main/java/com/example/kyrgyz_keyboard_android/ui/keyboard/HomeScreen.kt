package com.example.kyrgyz_keyboard_android.ui.keyboard

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.kyrgyz_keyboard_android.keyboard.model.CapsLockState
import com.example.kyrgyz_keyboard_android.keyboard.model.KeyboardLayout
import com.example.kyrgyz_keyboard_android.ui.keyboard.components.CapsLockRow
import com.example.kyrgyz_keyboard_android.ui.keyboard.components.KeyboardRow
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions.keyboardBottomPadding
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions.keyboardHorizontalPadding
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions.keyboardVerticalPadding
import com.example.kyrgyz_keyboard_android.ui.theme.KeyboardGray

@Composable
fun HomeScreen() {
    var capsLockEnabled by remember { mutableStateOf(CapsLockState.TEMPORARY) }
    LaunchedEffect(capsLockEnabled) {
        Log.d("CapsLock", "State changed to: $capsLockEnabled")
    }
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
            .padding(bottom = keyboardBottomPadding)
            .padding(horizontal = keyboardHorizontalPadding, vertical = keyboardVerticalPadding)
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

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
package com.example.kyrgyz_keyboard_android.ui.keyboard

import SuggestionsRow
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kyrgyz_keyboard_android.keyboard.model.CapsLockState
import com.example.kyrgyz_keyboard_android.keyboard.model.KeyboardLayout.row1
import com.example.kyrgyz_keyboard_android.keyboard.model.KeyboardLayout.row2
import com.example.kyrgyz_keyboard_android.keyboard.model.KeyboardLayout.row3
import com.example.kyrgyz_keyboard_android.keyboard.model.KeyboardLayout.row4
import com.example.kyrgyz_keyboard_android.keyboard.model.KeyboardLayout.row5
import com.example.kyrgyz_keyboard_android.keyboard.viewmodel.KeyboardViewModel
import com.example.kyrgyz_keyboard_android.ui.keyboard.components.CapsLockRow
import com.example.kyrgyz_keyboard_android.ui.keyboard.components.KeyboardRow
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions.keyboardBottomPadding
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions.keyboardHorizontalPadding
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions.keyboardVerticalPadding
import com.example.kyrgyz_keyboard_android.ui.theme.KeyboardGray

@Composable
fun HomeScreen(viewModel: KeyboardViewModel = viewModel()) {
    val capsLockEnabled by viewModel.capsLockState.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()
    val currentWord by viewModel.currentWord.collectAsState()
    val isMidWord by viewModel.isMidWord.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(KeyboardGray)
    ) {
        SuggestionsRow(
            suggestions = suggestions,
            viewModel = viewModel,
            modifier = Modifier.padding(bottom = keyboardVerticalPadding),
            isMidWord = isMidWord
        )

        KeyboardLayout(
            capsLockEnabled = capsLockEnabled,
            viewModel = viewModel
        )
    }
}

@Composable
fun KeyboardLayout(
    capsLockEnabled: CapsLockState, viewModel: KeyboardViewModel
) {
    Column(
        modifier = Modifier
            .background(color = KeyboardGray)
            .fillMaxWidth()
            .padding(bottom = keyboardBottomPadding)
            .padding(horizontal = keyboardHorizontalPadding, vertical = keyboardVerticalPadding)
    ) {
        KeyboardRow(keys = row1, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
        KeyboardRow(keys = row2, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
        KeyboardRow(keys = row3, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
        CapsLockRow(keys = row4, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
        KeyboardRow(keys = row5, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
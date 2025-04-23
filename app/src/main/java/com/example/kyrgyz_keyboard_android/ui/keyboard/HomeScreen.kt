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
import com.example.kyrgyz_keyboard_android.keyboard.model.KeyboardLayout
import com.example.kyrgyz_keyboard_android.keyboard.model.SymbolsLayout
import com.example.kyrgyz_keyboard_android.keyboard.viewmodel.KeyboardViewModel
import com.example.kyrgyz_keyboard_android.ui.keyboard.components.CapsLockRow
import com.example.kyrgyz_keyboard_android.ui.keyboard.components.KeyboardRow
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions
import com.example.kyrgyz_keyboard_android.ui.theme.KeyboardGray

@Composable
fun HomeScreen(viewModel: KeyboardViewModel = viewModel()) {
    val capsLockEnabled by viewModel.capsLockState.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()
    val currentWord by viewModel.currentWord.collectAsState()
    val isMidWord by viewModel.isMidWord.collectAsState()
    val isSymbolsMode by viewModel.isSymbolsMode.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(KeyboardGray)
    ) {
        SuggestionsRow(
            suggestions = suggestions,
            viewModel = viewModel,
            modifier = Modifier.padding(bottom = Dimensions.keyboardVerticalPadding),
            isMidWord = isMidWord
        )

        if (isSymbolsMode) {
            SymbolsLayout(viewModel)
        } else {
            KeyboardLayout(
                capsLockEnabled = capsLockEnabled,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun SymbolsLayout(
    viewModel: KeyboardViewModel
) {
    Column(
        modifier = Modifier
            .background(color = KeyboardGray)
            .fillMaxWidth()
            .padding(bottom = Dimensions.keyboardBottomPadding)
            .padding(horizontal = Dimensions.keyboardHorizontalPadding, vertical = Dimensions.keyboardVerticalPadding)
    ) {
        KeyboardRow(keys = SymbolsLayout.symbolsRow1, capsLockEnabled = CapsLockState.OFF, viewModel = viewModel)
        KeyboardRow(keys = SymbolsLayout.symbolsRow2, capsLockEnabled = CapsLockState.OFF, viewModel = viewModel)
        KeyboardRow(keys = SymbolsLayout.symbolsRow3, capsLockEnabled = CapsLockState.OFF, viewModel = viewModel)
        KeyboardRow(keys = SymbolsLayout.symbolsRow4, capsLockEnabled = CapsLockState.OFF, viewModel = viewModel)
        KeyboardRow(keys = SymbolsLayout.symbolsRow5, capsLockEnabled = CapsLockState.OFF, viewModel = viewModel)
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
            .padding(bottom = Dimensions.keyboardBottomPadding)
            .padding(horizontal = Dimensions.keyboardHorizontalPadding, vertical = Dimensions.keyboardVerticalPadding)
    ) {
        KeyboardRow(keys = KeyboardLayout.row1, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
        KeyboardRow(keys = KeyboardLayout.row2, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
        KeyboardRow(keys = KeyboardLayout.row3, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
        CapsLockRow(keys = KeyboardLayout.row4, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
        KeyboardRow(keys = KeyboardLayout.row5, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}

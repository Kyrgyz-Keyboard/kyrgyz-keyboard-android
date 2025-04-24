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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kyrgyz_keyboard_android.keyboard.model.CapsLockState
import com.example.kyrgyz_keyboard_android.keyboard.model.KeyboardLayout
import com.example.kyrgyz_keyboard_android.keyboard.model.SymbolsLayout1
import com.example.kyrgyz_keyboard_android.keyboard.model.SymbolsLayout2
import com.example.kyrgyz_keyboard_android.keyboard.viewmodel.KeyboardViewModel
import com.example.kyrgyz_keyboard_android.ui.keyboard.components.CapsLockRow
import com.example.kyrgyz_keyboard_android.ui.keyboard.components.KeyboardRow
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions
import com.example.kyrgyz_keyboard_android.ui.theme.KeyboardGray

@Composable
fun HomeScreen(viewModel: KeyboardViewModel = viewModel()) {
    val keyboardState by viewModel.keyboardState.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()

    KeyboardContainer {
        SuggestionsRow(
            suggestions = suggestions,
            viewModel = viewModel,
            modifier = Modifier.padding(bottom = Dimensions.keyboardVerticalPadding),
            isMidWord = keyboardState.isMidWord
        )

        when {
            keyboardState.isSymbolsMode && keyboardState.isSymbolsLayout2 -> SymbolsLayout2(viewModel)
            keyboardState.isSymbolsMode -> SymbolsLayout(viewModel)
            else -> KeyboardLayout(keyboardState.capsLockState, viewModel)
        }
    }
}

@Composable
private fun KeyboardContainer(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(KeyboardGray)
    ) {
        content()
    }
}

@Composable
private fun SymbolsLayout(viewModel: KeyboardViewModel) {
    KeyboardBase {
        KeyboardRow(keys = SymbolsLayout1.symbolsRow1, capsLockEnabled = CapsLockState.OFF, viewModel = viewModel)
        KeyboardRow(keys = SymbolsLayout1.symbolsRow2, capsLockEnabled = CapsLockState.OFF, viewModel = viewModel)
        KeyboardRow(keys = SymbolsLayout1.symbolsRow4, capsLockEnabled = CapsLockState.OFF, viewModel = viewModel)
        KeyboardRow(keys = SymbolsLayout1.symbolsRow5, capsLockEnabled = CapsLockState.OFF, viewModel = viewModel)
    }
}

@Composable
private fun SymbolsLayout2(viewModel: KeyboardViewModel) {
    KeyboardBase {
        KeyboardRow(keys = SymbolsLayout2.symbolsRow1, capsLockEnabled = CapsLockState.OFF, viewModel = viewModel)
        KeyboardRow(keys = SymbolsLayout2.symbolsRow2, capsLockEnabled = CapsLockState.OFF, viewModel = viewModel)
        KeyboardRow(keys = SymbolsLayout2.symbolsRow4, capsLockEnabled = CapsLockState.OFF, viewModel = viewModel)
        KeyboardRow(keys = SymbolsLayout2.symbolsRow5, capsLockEnabled = CapsLockState.OFF, viewModel = viewModel)
    }
}

@Composable
private fun KeyboardLayout(capsLockEnabled: CapsLockState, viewModel: KeyboardViewModel) {
    KeyboardBase {
        KeyboardRow(keys = KeyboardLayout.row1, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
        KeyboardRow(keys = KeyboardLayout.row2, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
        KeyboardRow(keys = KeyboardLayout.row3, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
        CapsLockRow(keys = KeyboardLayout.row4, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
        KeyboardRow(keys = KeyboardLayout.row5, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
    }
}

@Composable
private fun KeyboardBase(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .background(color = KeyboardGray)
            .fillMaxWidth()
            .padding(horizontal = Dimensions.keyboardHorizontalPadding,
                vertical = Dimensions.keyboardVerticalPadding)
            .padding(bottom = Dimensions.keyboardBottomPadding)
    ) {
        content()
    }
}
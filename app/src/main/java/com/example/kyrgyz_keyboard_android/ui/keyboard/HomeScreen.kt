package com.example.kyrgyz_keyboard_android.ui.keyboard

import SuggestionsRow
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kyrgyz_keyboard_android.keyboard.model.CapsLockState
import com.example.kyrgyz_keyboard_android.keyboard.model.CyrillicUiLayout
import com.example.kyrgyz_keyboard_android.keyboard.model.EnesayUiLayout
import com.example.kyrgyz_keyboard_android.keyboard.model.LatinLayout
import com.example.kyrgyz_keyboard_android.keyboard.model.SymbolsLayout1
import com.example.kyrgyz_keyboard_android.keyboard.model.SymbolsLayout2
import com.example.kyrgyz_keyboard_android.keyboard.viewmodel.KeyboardViewModel
import com.example.kyrgyz_keyboard_android.ui.keyboard.components.CapsLockRow
import com.example.kyrgyz_keyboard_android.ui.keyboard.components.KeyboardRow
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions
import com.example.kyrgyz_keyboard_android.ui.theme.KeyboardGray
import com.example.kyrgyz_keyboard_android.ui.theme.KeyboardGrayDark

@Composable
fun HomeScreen(viewModel: KeyboardViewModel = viewModel()) {
    val keyboardState by viewModel.keyboardState.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()
    val currentLayout = when {
        keyboardState.isEnesayLayout -> EnesayUiLayout
        keyboardState.isLatinLayout -> LatinLayout
        else -> CyrillicUiLayout
    }

    KeyboardContainer(viewModel) {
        SuggestionsRow(
            suggestions = suggestions,
            viewModel = viewModel,
            isMidWord = keyboardState.isMidWord
        )

        when {
            keyboardState.isSymbolsMode && keyboardState.isSymbolsLayout2 -> SymbolsLayout2(viewModel)
            keyboardState.isSymbolsMode -> SymbolsLayout(viewModel)
            else -> MainKeyboardLayout(keyboardState.capsLockState, viewModel, currentLayout)
        }
    }
}

@Composable
private fun KeyboardContainer(viewModel: KeyboardViewModel, content: @Composable () -> Unit) {
    val keyboardState by viewModel.keyboardState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (keyboardState.isDarkMode) KeyboardGrayDark else KeyboardGray)
    ) {
        content()
    }
}

@Composable
private fun SymbolsLayout(viewModel: KeyboardViewModel) {
    val keyboardState by viewModel.keyboardState.collectAsState()

    KeyboardBase(viewModel) {
        KeyboardRow(keys = SymbolsLayout1.symbolsRow1, capsLockEnabled = CapsLockState.OFF, viewModel = viewModel)
        KeyboardRow(keys = SymbolsLayout1.symbolsRow2, capsLockEnabled = CapsLockState.OFF, viewModel = viewModel)
        KeyboardRow(keys = SymbolsLayout1.symbolsRow3, capsLockEnabled = CapsLockState.OFF, viewModel = viewModel)
        KeyboardRow(keys = SymbolsLayout1.symbolsRow4, capsLockEnabled = CapsLockState.OFF, viewModel = viewModel)
        KeyboardRow(
            keys = SymbolsLayout1.getSymbolsRow5(keyboardState.isLatinLayout),
            capsLockEnabled = CapsLockState.OFF,
            viewModel = viewModel
        )
    }
}

@Composable
private fun SymbolsLayout2(viewModel: KeyboardViewModel) {
    val keyboardState by viewModel.keyboardState.collectAsState()

    KeyboardBase(viewModel) {
        KeyboardRow(keys = SymbolsLayout2.symbolsRow1, capsLockEnabled = CapsLockState.OFF, viewModel = viewModel)
        KeyboardRow(keys = SymbolsLayout2.symbolsRow2, capsLockEnabled = CapsLockState.OFF, viewModel = viewModel)
        KeyboardRow(keys = SymbolsLayout2.symbolsRow3, capsLockEnabled = CapsLockState.OFF, viewModel = viewModel)
        KeyboardRow(keys = SymbolsLayout2.symbolsRow4, capsLockEnabled = CapsLockState.OFF, viewModel = viewModel)
        KeyboardRow(
            keys = SymbolsLayout2.getSymbolsRow5(keyboardState.isLatinLayout),
            capsLockEnabled = CapsLockState.OFF,
            viewModel = viewModel
        )
    }
}

@Composable
private fun MainKeyboardLayout(
    capsLockEnabled: CapsLockState,
    viewModel: KeyboardViewModel,
    layout: Any
) {
    KeyboardBase(viewModel) {
        when (layout) {
            is CyrillicUiLayout -> {
                KeyboardRow(keys = CyrillicUiLayout.row1, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
                KeyboardRow(keys = CyrillicUiLayout.row2, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
                KeyboardRow(keys = CyrillicUiLayout.row3, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
                CapsLockRow(keys = CyrillicUiLayout.row4, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
                KeyboardRow(keys = CyrillicUiLayout.row5, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
            }
            is LatinLayout -> {
                KeyboardRow(keys = LatinLayout.row1, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
                KeyboardRow(keys = LatinLayout.row2, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
                KeyboardRow(keys = LatinLayout.row3, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
                CapsLockRow(keys = LatinLayout.row4, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
                KeyboardRow(keys = LatinLayout.row5, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
            }
            is EnesayUiLayout -> {
                KeyboardRow(keys = EnesayUiLayout.row1, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
                KeyboardRow(keys = EnesayUiLayout.row2, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
                KeyboardRow(keys = EnesayUiLayout.row3, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
                CapsLockRow(keys = EnesayUiLayout.row4, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
                KeyboardRow(keys = EnesayUiLayout.row5, capsLockEnabled = capsLockEnabled, viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun KeyboardBase(viewModel: KeyboardViewModel, content: @Composable () -> Unit) {
    val keyboardState by viewModel.keyboardState.collectAsState()
    Column(
        modifier = Modifier
            .background(color = if (keyboardState.isDarkMode) KeyboardGrayDark else KeyboardGray)
            .fillMaxWidth()
            .padding(
                horizontal = Dimensions.keyboardHorizontalPadding,
                vertical = Dimensions.keyboardVerticalPadding
            )
            .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 10.dp)
    ) {
        content()
    }
}
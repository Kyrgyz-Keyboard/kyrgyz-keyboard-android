package com.example.kyrgyz_keyboard_android.keyboard.dictionary

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.kyrgyz_keyboard_android.keyboard.service.KyrgyzKeyboardIME
import com.example.kyrgyz_keyboard_android.keyboard.viewmodel.KeyboardViewModel
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions
import com.example.kyrgyz_keyboard_android.ui.theme.KeyboardGray
import com.example.kyrgyz_keyboard_android.ui.theme.KeyboardGrayDark

@Composable
fun DictionaryLayout(
    viewModel: KeyboardViewModel, onClose: () -> Unit
) {
    val keyboardState by viewModel.keyboardState.collectAsState()
    val context = LocalContext.current

    var dictionaryHeight by remember { mutableStateOf(350.dp) }
    val minHeight = 200.dp
    val maxHeight = 500.dp
    val animatedHeight by animateDpAsState(targetValue = dictionaryHeight)

    val dictionaryWords = getDictionaryWords()
    val categories = dictionaryWords.map { it.category }.distinct()
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull() ?: "") }
    val filteredWords = dictionaryWords.filter { it.category == selectedCategory }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(animatedHeight)
            .background(if (keyboardState.isDarkMode) KeyboardGrayDark else KeyboardGray)
            .padding(
                horizontal = Dimensions.keyboardHorizontalPadding,
                vertical = Dimensions.keyboardVerticalPadding
            )
            .padding(
                bottom = WindowInsets.navigationBars.asPaddingValues()
                    .calculateBottomPadding() + 10.dp
            )
            .pointerInput(Unit) {
                detectVerticalDragGestures(onDragEnd = {
                    dictionaryHeight = if (dictionaryHeight > (minHeight + maxHeight) / 2) {
                        maxHeight
                    } else {
                        minHeight
                    }
                }, onDragCancel = {}, onVerticalDrag = { _, dragAmount ->
                    dictionaryHeight =
                        (dictionaryHeight - dragAmount.toDp()).coerceIn(minHeight, maxHeight)
                })
            }) {
        DragHandle(isDarkMode = keyboardState.isDarkMode)

        DictionaryHeader(
            isDarkMode = keyboardState.isDarkMode, onClose = onClose
        )

        CategoryTabs(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it },
            isDarkMode = keyboardState.isDarkMode
        )

        Spacer(modifier = Modifier.height(8.dp))

        WordsGrid(
            words = filteredWords, isDarkMode = keyboardState.isDarkMode, onWordClick = { word ->
                val inputConnection = (context as? KyrgyzKeyboardIME)?.currentInputConnection
                inputConnection?.commitText(word.word, 1)
                onClose()
            })
    }
}

@Composable
private fun DragHandle(isDarkMode: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(if (isDarkMode) Color.DarkGray else Color.LightGray)
        )
    }
}


@Composable
private fun Float.toDp() = with(LocalDensity.current) { this@toDp.toDp() }
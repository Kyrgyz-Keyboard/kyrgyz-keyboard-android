package com.example.kyrgyz_keyboard_android.keyboard.dictionary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.kyrgyz_keyboard_android.ui.theme.*

@Composable
fun EnhancedWordsGrid(
    words: List<DictionaryWord>,
    isDarkMode: Boolean,
    screenWidth: Dp,
    onWordClick: (DictionaryWord) -> Unit
) {
    val columnCount = when {
        screenWidth < 360.dp -> 2  // Small phones
        screenWidth < 600.dp -> 3  // Regular phones
        screenWidth < 840.dp -> 4  // Large phones/small tablets
        else -> 5                  // Tablets
    }

    if (words.isEmpty()) {
        EmptyState(isDarkMode = isDarkMode)
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(columnCount),
            modifier = Modifier
                .fillMaxWidth()
                .background(if (isDarkMode) KeyboardGrayDark else KeyboardGray),
            contentPadding = PaddingValues(Dimensions.dictionaryGridSpacing / 2),
            verticalArrangement = Arrangement.spacedBy(Dimensions.dictionaryGridSpacing),
            horizontalArrangement = Arrangement.spacedBy(Dimensions.dictionaryGridSpacing)
        ) {
            items(words) { word ->
                EnhancedDictionaryWordItem(
                    word = word,
                    isDarkMode = isDarkMode,
                    onClick = { onWordClick(word) }
                )
            }
        }
    }
}

@Composable
private fun EmptyState(isDarkMode: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "🔍 Сөздөр табылган жок",
            style = keyboardTextStyle.copy(
                fontSize = Dimensions.dictionaryTranslationSize
            ),
            color = if (isDarkMode) KeyTextColorDark.copy(alpha = 0.6f) 
                   else KeyTextColor.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun WordsGrid(
    words: List<DictionaryWord>,
    isDarkMode: Boolean,
    onWordClick: (DictionaryWord) -> Unit
) {
    EnhancedWordsGrid(
        words = words,
        isDarkMode = isDarkMode,
        screenWidth = 360.dp, // Default phone size
        onWordClick = onWordClick
    )
}
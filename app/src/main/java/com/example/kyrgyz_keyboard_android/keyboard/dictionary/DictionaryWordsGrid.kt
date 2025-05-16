package com.example.kyrgyz_keyboard_android.keyboard.dictionary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kyrgyz_keyboard_android.ui.theme.KeyboardGray
import com.example.kyrgyz_keyboard_android.ui.theme.KeyboardGrayDark

@Composable
fun WordsGrid(
    words: List<DictionaryWord>,
    isDarkMode: Boolean,
    onWordClick: (DictionaryWord) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
            .background(if (isDarkMode) KeyboardGrayDark else KeyboardGray),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(words) { word ->
            DictionaryWordItem(
                word = word,
                isDarkMode = isDarkMode,
                onClick = { onWordClick(word) }
            )
        }
    }
}
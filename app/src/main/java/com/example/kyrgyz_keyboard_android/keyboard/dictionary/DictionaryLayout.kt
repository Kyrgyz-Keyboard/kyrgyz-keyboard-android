package com.example.kyrgyz_keyboard_android.keyboard.dictionary

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
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
    viewModel: KeyboardViewModel, 
    onClose: () -> Unit
) {
    val keyboardState by viewModel.keyboardState.collectAsState()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    
    val adaptiveMinHeight = (screenHeight * 0.25f).coerceAtLeast(Dimensions.dictionaryMinHeight)
    val adaptiveMaxHeight = (screenHeight * 0.65f).coerceAtMost(Dimensions.dictionaryMaxHeight)
    val adaptiveDefaultHeight = (screenHeight * 0.45f).coerceIn(adaptiveMinHeight, adaptiveMaxHeight)

    var dictionaryHeight by remember { mutableStateOf(adaptiveDefaultHeight) }
    val animatedHeight by animateDpAsState(
        targetValue = dictionaryHeight,
        animationSpec = tween(durationMillis = 300)
    )

    val dictionaryWords = getDictionaryWords()
    val categories = dictionaryWords.map { it.category }.distinct()
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull() ?: "") }
    var searchQuery by remember { mutableStateOf("") }
    
    val filteredWords = dictionaryWords.filter { word ->
        word.category == selectedCategory && 
        (searchQuery.isEmpty() || 
         word.word.contains(searchQuery, ignoreCase = true) ||
         word.translation.contains(searchQuery, ignoreCase = true))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(animatedHeight)
            .background(
                color = if (keyboardState.isDarkMode) KeyboardGrayDark else KeyboardGray,
                shape = RoundedCornerShape(
                    topStart = Dimensions.dictionaryCornerRadius,
                    topEnd = Dimensions.dictionaryCornerRadius
                )
            )
            .padding(
                horizontal = Dimensions.keyboardHorizontalPadding,
                vertical = Dimensions.keyboardVerticalPadding
            )
            .padding(
                bottom = WindowInsets.navigationBars.asPaddingValues()
                    .calculateBottomPadding() + 10.dp
            )
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragEnd = {
                        dictionaryHeight = if (dictionaryHeight > (adaptiveMinHeight + adaptiveMaxHeight) / 2) {
                            adaptiveMaxHeight
                        } else {
                            adaptiveMinHeight
                        }
                    },
                    onVerticalDrag = { _, dragAmount ->
                        dictionaryHeight = (dictionaryHeight - dragAmount.toDp())
                            .coerceIn(adaptiveMinHeight, adaptiveMaxHeight)
                    }
                )
            }
    ) {
        EnhancedDragHandle(isDarkMode = keyboardState.isDarkMode)

        EnhancedDictionaryHeader(
            isDarkMode = keyboardState.isDarkMode, 
            onClose = onClose
        )

        Spacer(modifier = Modifier.height(8.dp))

//        EnhancedSearchBar(
//            searchQuery = searchQuery,
//            onSearchQueryChange = { searchQuery = it },
//            isDarkMode = keyboardState.isDarkMode
//        )

        Spacer(modifier = Modifier.height(8.dp))

        EnhancedCategoryTabs(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it },
            isDarkMode = keyboardState.isDarkMode
        )

        Spacer(modifier = Modifier.height(Dimensions.dictionaryGridSpacing))

        EnhancedWordsGrid(
            words = filteredWords, 
            isDarkMode = keyboardState.isDarkMode,
            screenWidth = configuration.screenWidthDp.dp,
            onWordClick = { word ->
                val inputConnection = (context as? KyrgyzKeyboardIME)?.currentInputConnection
                inputConnection?.commitText(word.word, 1)
                onClose()
            }
        )
    }
}

@Composable
private fun EnhancedDragHandle(isDarkMode: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(Dimensions.dictionaryDragHandleWidth)
                .height(Dimensions.dictionaryDragHandleHeight)
                .clip(RoundedCornerShape(Dimensions.dictionaryDragHandleHeight / 2))
                .background(
                    if (isDarkMode) Color.White.copy(alpha = 0.3f) 
                    else Color.Black.copy(alpha = 0.3f)
                )
        )
    }
}

@Composable
private fun Float.toDp() = with(LocalDensity.current) { this@toDp.toDp() }
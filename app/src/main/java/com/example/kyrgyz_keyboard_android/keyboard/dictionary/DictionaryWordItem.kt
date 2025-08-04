package com.example.kyrgyz_keyboard_android.keyboard.dictionary

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.kyrgyz_keyboard_android.ui.theme.*

@Composable
fun EnhancedDictionaryWordItem(
    word: DictionaryWord,
    isDarkMode: Boolean,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed) {
            if (isDarkMode) KeyTapBackgroundColorDark else KeyTapBackgroundColor
        } else {
            if (isDarkMode) KeyBackgroundColorDark else KeyBackgroundColor
        },
        animationSpec = tween(durationMillis = 150)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = Dimensions.dictionaryWordItemMinHeight)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    color = if (isDarkMode) Color.White else Color.Black
                ),
                onClickLabel = "Сөздү тандоо"
            ) {
                onClick()
            },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        ),
        shape = RoundedCornerShape(Dimensions.dictionaryCornerRadius)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.dictionaryWordItemPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = word.word,
                style = keyboardTextStyle.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = Dimensions.dictionaryWordSize,
                    textAlign = TextAlign.Center
                ),
                color = if (isDarkMode) KeyTextColorDark else KeyTextColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = word.translation,
                style = keyboardTextStyle.copy(
                    fontSize = Dimensions.dictionaryTranslationSize,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal
                ),
                color = if (isDarkMode) KeyTextColorDark.copy(alpha = 0.8f) 
                       else KeyTextColor.copy(alpha = 0.8f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun DictionaryWordItem(
    word: DictionaryWord,
    isDarkMode: Boolean,
    onClick: () -> Unit
) {
    EnhancedDictionaryWordItem(word, isDarkMode, onClick)
}
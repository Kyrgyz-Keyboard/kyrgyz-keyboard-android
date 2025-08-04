package com.example.kyrgyz_keyboard_android.keyboard.dictionary

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun EnhancedCategoryTabs(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    isDarkMode: Boolean
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimensions.dictionaryTabHeight),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(categories) { category ->
            EnhancedCategoryTab(
                category = category,
                isSelected = category == selectedCategory,
                onClick = { onCategorySelected(category) },
                isDarkMode = isDarkMode
            )
        }
    }
}

@Composable
private fun EnhancedCategoryTab(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    isDarkMode: Boolean
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            if (isDarkMode) GBoardBlue.copy(alpha = 0.8f) else GBoardBlue
        } else {
            if (isDarkMode) KeyBackgroundColorDark else KeyBackgroundColor
        },
        animationSpec = tween(durationMillis = 200)
    )
    
    val textColor by animateColorAsState(
        targetValue = if (isSelected) {
            Color.White
        } else {
            if (isDarkMode) KeyTextColorDark else KeyTextColor
        },
        animationSpec = tween(durationMillis = 200)
    )

    Card(
        modifier = Modifier
            .height(Dimensions.dictionaryTabHeight - 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 1.dp
        ),
        shape = RoundedCornerShape(Dimensions.dictionaryTabHeight / 2)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category,
                style = keyboardTextStyle.copy(
                    fontSize = Dimensions.dictionaryTabSize,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                ),
                color = textColor,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun CategoryTabs(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    isDarkMode: Boolean
) {
    EnhancedCategoryTabs(categories, selectedCategory, onCategorySelected, isDarkMode)
}

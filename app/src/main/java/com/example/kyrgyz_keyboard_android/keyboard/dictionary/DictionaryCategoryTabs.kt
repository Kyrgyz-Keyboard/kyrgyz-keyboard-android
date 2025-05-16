package com.example.kyrgyz_keyboard_android.keyboard.dictionary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kyrgyz_keyboard_android.ui.theme.KeyBackgroundColor
import com.example.kyrgyz_keyboard_android.ui.theme.KeyBackgroundColorDark
import com.example.kyrgyz_keyboard_android.ui.theme.KeyTextColor
import com.example.kyrgyz_keyboard_android.ui.theme.KeyTextColorDark
import com.example.kyrgyz_keyboard_android.ui.theme.keyboardTextStyle

@Composable
fun CategoryTabs(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    isDarkMode: Boolean
) {
    TabRow(
        selectedTabIndex = categories.indexOf(selectedCategory).coerceAtLeast(0),
        containerColor = if (isDarkMode) KeyBackgroundColorDark else KeyBackgroundColor,
        contentColor = if (isDarkMode) KeyTextColorDark else KeyTextColor,
        indicator = { tabPositions ->
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[categories.indexOf(selectedCategory).coerceAtLeast(0)])
                    .height(3.dp)
                    .background(
                        color = if (isDarkMode) Color.White else Color.Blue,
                        shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)
                    )
            )
        }
    ) {
        categories.forEach { category ->
            Tab(
                selected = category == selectedCategory,
                onClick = { onCategorySelected(category) },
                text = {
                    Text(
                        text = category,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = keyboardTextStyle.copy(fontSize = 14.sp),
                        color = if (isDarkMode) KeyTextColorDark else KeyTextColor
                    )
                },
                modifier = Modifier.background(if (isDarkMode) KeyBackgroundColorDark else KeyBackgroundColor)
            )
        }
    }
}

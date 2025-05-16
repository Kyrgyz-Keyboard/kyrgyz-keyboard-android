package com.example.kyrgyz_keyboard_android.keyboard.dictionary

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions
import com.example.kyrgyz_keyboard_android.ui.theme.KeyBackgroundColor
import com.example.kyrgyz_keyboard_android.ui.theme.KeyBackgroundColorDark
import com.example.kyrgyz_keyboard_android.ui.theme.KeyTextColor
import com.example.kyrgyz_keyboard_android.ui.theme.KeyTextColorDark
import com.example.kyrgyz_keyboard_android.ui.theme.keyboardTextStyle

@Composable
fun DictionaryWordItem(
    word: DictionaryWord,
    isDarkMode: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimensions.keyCornerRadius))
            .background(if (isDarkMode) KeyBackgroundColorDark else KeyBackgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .padding(bottom = 2.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column {
            Text(
                text = word.word,
                style = keyboardTextStyle.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start
                ),
                color = if (isDarkMode) KeyTextColorDark else KeyTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = word.translation,
                style = keyboardTextStyle.copy(
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    textAlign = TextAlign.Start
                ),
                color = if (isDarkMode) KeyTextColorDark.copy(alpha = 0.7f) else KeyTextColor.copy(alpha = 0.7f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
package com.example.kyrgyz_keyboard_android.keyboard.dictionary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions
import com.example.kyrgyz_keyboard_android.ui.theme.KeyBackgroundColor
import com.example.kyrgyz_keyboard_android.ui.theme.KeyBackgroundColorDark
import com.example.kyrgyz_keyboard_android.ui.theme.KeyTextColor
import com.example.kyrgyz_keyboard_android.ui.theme.KeyTextColorDark
import com.example.kyrgyz_keyboard_android.ui.theme.keyboardTextStyle

@Composable
fun EnhancedDictionaryHeader(
    isDarkMode: Boolean, onClose: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimensions.dictionaryHeaderHeight),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode) KeyBackgroundColorDark else KeyBackgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(Dimensions.dictionaryCornerRadius)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = onClose, modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Артка",
                        tint = if (isDarkMode) KeyTextColorDark else KeyTextColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "📚 Сөздүк", style = keyboardTextStyle.copy(
                        fontWeight = FontWeight.Bold, fontSize = Dimensions.dictionaryTitleSize
                    ), color = if (isDarkMode) KeyTextColorDark else KeyTextColor
                )
            }

            Text(
                text = "булак: tilimpoz", style = keyboardTextStyle.copy(
                    fontWeight = FontWeight.Normal, fontSize = Dimensions.dictionarySubtitleSize
                ), color = if (isDarkMode) KeyTextColorDark.copy(alpha = 0.7f)
                else KeyTextColor.copy(alpha = 0.7f)
            )
        }
    }
}
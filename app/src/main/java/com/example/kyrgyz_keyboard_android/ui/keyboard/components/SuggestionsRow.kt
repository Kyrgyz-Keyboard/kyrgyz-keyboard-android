import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.kyrgyz_keyboard_android.keyboard.input.handleSuggestionClick
import com.example.kyrgyz_keyboard_android.keyboard.viewmodel.KeyboardViewModel
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions.keyCornerRadius
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions.keyboardHorizontalPadding
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions.keyboardVerticalPadding
import com.example.kyrgyz_keyboard_android.ui.theme.KeyboardGray
import com.example.kyrgyz_keyboard_android.ui.theme.keyboardTextStyle

@Composable
fun SuggestionsRow(
    suggestions: List<String>,
    viewModel: KeyboardViewModel,
    modifier: Modifier = Modifier,
    isMidWord: Boolean = false
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(KeyboardGray)
            .padding(horizontal = keyboardHorizontalPadding, vertical = keyboardVerticalPadding),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        suggestions.forEach { suggestion ->
            SuggestionChip(
                text = suggestion, onClick = {
                    handleSuggestionClick(suggestion, context, viewModel)
                })
        }
    }
}

@Composable
fun SuggestionChip(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(keyCornerRadius))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = text, style = keyboardTextStyle, color = Color.DarkGray
        )
    }
}
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.kyrgyz_keyboard_android.keyboard.viewmodel.KeyboardViewModel
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions.keyCornerRadius
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions.keyboardHorizontalPadding
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions.keyboardVerticalPadding
import com.example.kyrgyz_keyboard_android.ui.theme.KeyTextColor
import com.example.kyrgyz_keyboard_android.ui.theme.KeyboardGray
import com.example.kyrgyz_keyboard_android.ui.theme.keyboardTextStyle

@Composable
fun SuggestionsRow(
    suggestions: List<String>,
    viewModel: KeyboardViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(KeyboardGray)
            .padding(horizontal = keyboardHorizontalPadding, vertical = keyboardVerticalPadding),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        suggestions.forEach { suggestion ->
            Text(
                text = suggestion,
                modifier = Modifier
                    .clip(RoundedCornerShape(keyCornerRadius))
                    .clickable { viewModel.onSuggestionSelected(suggestion) }
                    .padding(keyboardHorizontalPadding),
                style = keyboardTextStyle,
                color = KeyTextColor
            )
        }
    }
}
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.kyrgyz_keyboard_android.keyboard.input.handleSuggestionClick
import com.example.kyrgyz_keyboard_android.keyboard.model.CapsLockState
import com.example.kyrgyz_keyboard_android.keyboard.viewmodel.KeyboardViewModel
import com.example.kyrgyz_keyboard_android.ui.theme.KeyboardGray
import com.example.kyrgyz_keyboard_android.ui.theme.keyboardTextStyle
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions
import com.example.kyrgyz_keyboard_android.ui.theme.KeyboardGrayDark

@Composable
fun SuggestionsRow(
    suggestions: List<String>,
    viewModel: KeyboardViewModel,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val keyboardState by viewModel.keyboardState.collectAsState()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(if (keyboardState.isDarkMode) KeyboardGrayDark else KeyboardGray)
            .padding(horizontal = Dimensions.keyboardHorizontalPadding, vertical = Dimensions.keyboardVerticalPadding)
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        val capsLockEnabled = keyboardState.capsLockState

        val shouldCapitalize = keyboardState.currentWord.isNotEmpty() &&
                keyboardState.currentWord.first().isUpperCase() && capsLockEnabled != CapsLockState.LOCKED

        suggestions.forEach { suggestion ->
            val text = when {
                shouldCapitalize || capsLockEnabled == CapsLockState.TEMPORARY ->
                    suggestion.replaceFirstChar { it.uppercase() }
                capsLockEnabled == CapsLockState.LOCKED ->
                    suggestion.uppercase()
                else ->
                    suggestion.lowercase()
            }
            
            SuggestionChip(
                text = text,
                isDarkMode = keyboardState.isDarkMode, 
                onClick = { handleSuggestionClick(text, context, viewModel) }
            )
        }
    }
}

@Composable
fun SuggestionChip(text: String, isDarkMode: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(Dimensions.keyCornerRadius))
            .clickable(onClick = onClick)
            .padding(horizontal = Dimensions.suggestionsHorizontalPadding, vertical = Dimensions.suggestionsVerticalPadding),
            contentAlignment = Alignment.Center
    ) {
        Text(
            text = text, fontSize = 18.sp, style = keyboardTextStyle, color = if (isDarkMode) Color.LightGray else Color.DarkGray
        )
    }
}
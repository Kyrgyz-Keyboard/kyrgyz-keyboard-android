package com.example.kyrgyz_keyboard_android

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kyrgyz_keyboard_android.ui.theme.KeyGray
import com.example.kyrgyz_keyboard_android.ui.theme.KeyboardGray

class KeyUiModel(
    var isSpecial: Boolean = false,
    var ch: String? = null,
    var img: Int? = null,
    var spaceBtn: Boolean = false,
    var weight: Float? = null
) {
    fun copy(
        isSpecial: Boolean = this.isSpecial,
        ch: String? = this.ch,
        img: Int? = this.img,
        spaceBtn: Boolean = this.spaceBtn,
        weight: Float? = this.weight
    ) = KeyUiModel(isSpecial, ch, img, spaceBtn, weight)
}

val row2 = listOf(
    KeyUiModel(ch = "й"),
    KeyUiModel(ch = "ц"),
    KeyUiModel(ch = "у"),
    KeyUiModel(ch = "к"),
    KeyUiModel(ch = "е"),
    KeyUiModel(ch = "н"),
    KeyUiModel(ch = "г"),
    KeyUiModel(ch = "ш"),
    KeyUiModel(ch = "о"),
    KeyUiModel(ch = "з"),
    KeyUiModel(ch = "х")
)

val row3 = listOf(
    KeyUiModel(ch = "ф"),
    KeyUiModel(ch = "ы"),
    KeyUiModel(ch = "в"),
    KeyUiModel(ch = "а"),
    KeyUiModel(ch = "п"),
    KeyUiModel(ch = "р"),
    KeyUiModel(ch = "ө"),
    KeyUiModel(ch = "л"),
    KeyUiModel(ch = "д"),
    KeyUiModel(ch = "ж"),
    KeyUiModel(ch = "э")
)

val row4 = listOf(
    KeyUiModel(isSpecial = true, img = R.drawable.ic_caps),
    KeyUiModel(ch = "я"),
    KeyUiModel(ch = "ч"),
    KeyUiModel(ch = "с"),
    KeyUiModel(ch = "м"),
    KeyUiModel(ch = "и"),
    KeyUiModel(ch = "т"),
    KeyUiModel(ch = "ү"),
    KeyUiModel(ch = "ң"),
    KeyUiModel(ch = "б"),
    KeyUiModel(ch = "ю"),
    KeyUiModel(isSpecial = true, img = R.drawable.ic_remove)
)

val row5 = listOf(
    KeyUiModel(isSpecial = true, img = R.drawable.ic_more),
    KeyUiModel(ch = "Кыргызча", spaceBtn = true),
    KeyUiModel(ch = " , "),
    KeyUiModel(ch = " . ")
)


@Composable
fun HomeScreen() {
    Keyboard(row2, row3, row4, row5)
}
//
//@Composable
//fun Keyboard(row2: List<KeyUiModel>, row3: List<KeyUiModel>, row4: List<KeyUiModel>, row5: List<KeyUiModel>) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Bottom,
//        modifier = Modifier
//            .background(color = KeyboardGray)
//            .padding(2.dp)
//    ) {
//        LazyRow(modifier = Modifier.padding(1.dp),
//            horizontalArrangement = Arrangement.spacedBy(2.dp)) {
//            items(row2) { key ->
//                if (!key.isSpecial) {
//                    Key(key)
//                }
//            }
//        }
//        LazyRow(modifier = Modifier.padding(1.dp),
//            horizontalArrangement = Arrangement.spacedBy(2.dp)) {
//            items(row3) { key ->
//                Key(key)
//            }
//        }
//        LazyRow(modifier = Modifier.padding(1.dp),
//            horizontalArrangement = Arrangement.spacedBy(2.dp),
//            verticalAlignment = Alignment.CenterVertically) {
//            items(row4) { key ->
//                Key(key)
//            }
//        }
//        LazyRow(modifier = Modifier.padding(1.dp),
//            horizontalArrangement = Arrangement.spacedBy(2.dp),
//            verticalAlignment = Alignment.CenterVertically) {
//            items(row5) { key ->
//                Key(key)
//            }
//        }
//    }
//}

@Composable
fun KeyboardRow(keys: List<KeyUiModel>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        keys.forEach { key ->
            Box(
                modifier = Modifier
                    .weight(key.weight ?: 1f)
                    .height(48.dp)
            ) {
                Key(key)
            }
        }
    }
}

@Composable
fun Keyboard(
    row2: List<KeyUiModel>, row3: List<KeyUiModel>, row4: List<KeyUiModel>, row5: List<KeyUiModel>
) {
    Column(
        modifier = Modifier
            .background(color = KeyboardGray)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Row 2
        KeyboardRow(row2)

        // Row 3
        KeyboardRow(row3)

        // Row 4
        KeyboardRow(row4.mapIndexed { index, key ->
            if (index == 0 || index == row4.size - 1) key.copy(weight = 1.5f) else key
        })

        // Row 5
        KeyboardRow(row5.map { key ->
            when {
                key.spaceBtn -> key.copy(weight = 6f)
                key.isSpecial -> key.copy(weight = 1.5f)
                else -> key.copy(weight = 1f)
            }
        })
    }
}

@Composable
fun Key(key: KeyUiModel) {
    val interactionSource = remember { MutableInteractionSource() }
    val ctx = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(
                color = if (key.isSpecial) KeyGray else Color.White,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(
                interactionSource = interactionSource, indication = null
            ) {
                if (!key.isSpecial && key.ch != null) {
                    (ctx as KyrgyzKeyboardIME).currentInputConnection.commitText(
                        key.ch.toString(), key.ch.toString().length
                    )
                }
            }, contentAlignment = Alignment.Center
    ) {
        if (!key.isSpecial) {
            Text(
                text = key.ch ?: "",
                fontSize = 18.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(5.dp)
            )
        } else if (key.img != null) {
            Image(
                painter = painterResource(id = key.img!!),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

//
//@Composable
//fun Key(key: KeyUiModel, weight: Float = 1f) {
//    val interactionSource = remember { MutableInteractionSource() }
//    val pressed = interactionSource.collectIsPressedAsState()
//    val ctx = LocalContext.current
//
//    Box(
//        modifier = Modifier
////            .weight(weight)
//            .height(48.dp) // Fixed height for all keys
//            .padding(2.dp)
//            .background(
//                color = if (key.isSpecial) KeyGray else Color.White,
//                shape = RoundedCornerShape(4.dp)
//            )
//            .clickable(
//                interactionSource = interactionSource,
//                indication = null
//            ) {
//                if (!key.isSpecial && key.ch != null) {
//                    (ctx as KyrgyzKeyboardIME).currentInputConnection.commitText(
//                        key.ch.toString(),
//                        key.ch.toString().length
//                    )
//                }
//                // Handle special keys here if needed
//            },
//        contentAlignment = Alignment.Center
//    ) {
//        if (!key.isSpecial) {
//            Text(
//                text = key.ch.toString(),
//                fontSize = 18.sp,
//                color = Color.Black,
//                textAlign = TextAlign.Center,
//                modifier = Modifier
//                    .background(color = Color.White, shape = RoundedCornerShape(2.dp))
//                    .padding(5.dp)
//                    .wrapContentWidth()
//                    .wrapContentHeight()
//                    .clickable(interactionSource = interactionSource, indication = null) {
//                        (ctx as KyrgyzKeyboardIME).currentInputConnection.commitText(
//                            key.ch.toString(),
//                            key.ch.toString().length
//                        )
//                    }
//
//            )
//        } else if (key.img != null) {
//            Image(
//                painter = painterResource(id = key.img!!),
//                contentDescription = null,
//                contentScale = ContentScale.Fit,
//                modifier = Modifier.size(24.dp)
//            )
//        }
//    }
//
//
////
////    if (!key.isSpecial) {
////        val keyModifier = Modifier
////            .background(color = Color.White, shape = RoundedCornerShape(2.dp))
////            .padding(5.dp)
////            .wrapContentWidth()
////            .wrapContentHeight()
////            .clickable(interactionSource = interactionSource, indication = null) {
////                (ctx as KyrgyzKeyboardIME).currentInputConnection.commitText(
////                    key.ch.toString(),
////                    key.ch.toString().length)
////            }
////
////        if (key.spaceBtn) {
////            Text(
////                text = key.ch.toString(),
////                fontSize = 10.sp,
////                color = Color.Black,
////                textAlign = TextAlign.Center,
////                modifier = keyModifier.width(100.dp)
////            )
////        } else {
////            Text(
////                text = key.ch.toString(),
////                fontSize = 10.sp,
////                color = Color.Black,
////                textAlign = TextAlign.Center,
////                modifier = keyModifier
////            )
////        }
////    } else {
////        Box(
////            modifier = Modifier
////                .background(color = KeyGray, shape = RoundedCornerShape(2.dp))
////                .padding(5.dp)
////                .wrapContentWidth()
////                .wrapContentHeight()
////                .clickable(interactionSource = interactionSource, indication = null) {
////                    (ctx as KyrgyzKeyboardIME).currentInputConnection.commitText(
////                        key.ch.toString(),
////                        key.ch.toString().length)}
////        ) {
////            Image(
////                painter = painterResource(id = key.img!!),
////                contentDescription = null,
////                contentScale = ContentScale.Fit,
////                modifier = Modifier.size(12.dp)
////            )
////        }
////    }
//    if (pressed.value) {
//        Text(
//            key.ch.toString(),
//            Modifier
//                .fillMaxWidth()
//                .background(color = Color.White, shape = RoundedCornerShape(2.dp))
//                .padding(
//                    start = 16.dp,
//                    end = 16.dp,
//                    top = 16.dp,
//                    bottom = 48.dp
//                )
//        )
//    }
//}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
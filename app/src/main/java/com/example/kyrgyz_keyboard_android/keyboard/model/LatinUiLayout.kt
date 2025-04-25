package com.example.kyrgyz_keyboard_android.keyboard.model

import com.example.kyrgyz_keyboard_android.R
import com.example.kyrgyz_keyboard_android.ui.keyboard.utils.KeyboardConstants

object LatinLayout {
    val row1 = listOf(
        KeyUiModel(isSpecial = true, img = R.drawable.ic_globe),
        KeyUiModel(isSpecial = true, ch = KeyboardConstants.ENESAY_CHARACTER, weight = 2f),
        KeyUiModel(ch = "ñ"),
        KeyUiModel(ch = "ö"),
        KeyUiModel(ch = "ü"),
        KeyUiModel(isSpecial = true, ch = KeyboardConstants.LATIN_DICT, weight = 2f),
        KeyUiModel(isSpecial = true, img = R.drawable.ic_emoji)
    )

    val row2 = listOf(
        KeyUiModel(ch = "q"),
        KeyUiModel(ch = "w"),
        KeyUiModel(ch = "e"),
        KeyUiModel(ch = "r"),
        KeyUiModel(ch = "t"),
        KeyUiModel(ch = "y"),
        KeyUiModel(ch = "u"),
        KeyUiModel(ch = "i"),
        KeyUiModel(ch = "o"),
        KeyUiModel(ch = "p")
    )

    val row3 = listOf(
        KeyUiModel(ch = "a"),
        KeyUiModel(ch = "s"),
        KeyUiModel(ch = "d"),
        KeyUiModel(ch = "f"),
        KeyUiModel(ch = "g"),
        KeyUiModel(ch = "h"),
        KeyUiModel(ch = "j"),
        KeyUiModel(ch = "k"),
        KeyUiModel(ch = "l")
    )

    val row4 = listOf(
        KeyUiModel(isSpecial = true, img = R.drawable.ic_caps, weight = 1.4f),
        KeyUiModel(ch = "z"),
        KeyUiModel(ch = "x"),
        KeyUiModel(ch = "c"),
        KeyUiModel(ch = "v"),
        KeyUiModel(ch = "b"),
        KeyUiModel(ch = "n"),
        KeyUiModel(ch = "m"),
        KeyUiModel(isSpecial = true, img = R.drawable.ic_remove, weight = 1.4f)
    )

    val row5 = listOf(
        KeyUiModel(isSpecial = true, ch = KeyboardConstants.SYMBOLS_CHARACTER, weight = 1.4f),
        KeyUiModel(ch = ","),
        KeyUiModel(ch = KeyboardConstants.LATIN_SPACE, weight = 5f),
        KeyUiModel(ch = "."),
        KeyUiModel(isSpecial = true, img = R.drawable.ic_enter, weight = 2f)
    )
}

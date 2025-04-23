package com.example.kyrgyz_keyboard_android.keyboard.model

import com.example.kyrgyz_keyboard_android.R
import com.example.kyrgyz_keyboard_android.ui.keyboard.utils.KeyboardConstants

object SymbolsLayout {
    val symbolsRow1 = listOf(
        KeyUiModel(ch = "1"), KeyUiModel(ch = "2"), KeyUiModel(ch = "3"),
        KeyUiModel(ch = "4"), KeyUiModel(ch = "5"), KeyUiModel(ch = "6"),
        KeyUiModel(ch = "7"), KeyUiModel(ch = "8"), KeyUiModel(ch = "9"),
        KeyUiModel(ch = "0")
    )

    val symbolsRow2 = listOf(
        KeyUiModel(ch = "@"), KeyUiModel(ch = "#"),
        KeyUiModel(ch = "\u2286"),
//        KeyUiModel(ch = "\u20C0"),
        KeyUiModel(ch = "$"), KeyUiModel(ch = "%"), KeyUiModel(ch = "&"),
        KeyUiModel(ch = "-"), KeyUiModel(ch = "+"), KeyUiModel(ch = "("),
        KeyUiModel(ch = ")")
    )

    val symbolsRow3 = listOf(
        KeyUiModel(ch = "*"), KeyUiModel(ch = "\""), KeyUiModel(ch = "'"),
        KeyUiModel(ch = ":"), KeyUiModel(ch = ";"), KeyUiModel(ch = "_"),
        KeyUiModel(ch = "^"), KeyUiModel(ch = "!"), KeyUiModel(ch = "?"),
        KeyUiModel(ch = "=")
    )

    val symbolsRow4 = listOf(
        KeyUiModel(ch = "/"),
        KeyUiModel(ch = "<"), KeyUiModel(ch = "~"), KeyUiModel(ch = "`"),
        KeyUiModel(ch = "{"), KeyUiModel(ch = "}"), KeyUiModel(ch = "["),
        KeyUiModel(ch = "]"), KeyUiModel(ch = "\\"), KeyUiModel(ch = "|"),
        KeyUiModel(isSpecial = true, img = R.drawable.ic_remove, weight = 1.5f)
    )

    val symbolsRow5 = listOf(
        KeyUiModel(isSpecial = true, ch = KeyboardConstants.SYMBOLS_CHARACTER, weight = 1.5f),
        KeyUiModel(ch = ","),
        KeyUiModel(ch = KeyboardConstants.SPACE_CHARACTER, weight = 5f),
        KeyUiModel(ch = "."),
        KeyUiModel(isSpecial = true, img = R.drawable.ic_enter, weight = 2f)
    )
}
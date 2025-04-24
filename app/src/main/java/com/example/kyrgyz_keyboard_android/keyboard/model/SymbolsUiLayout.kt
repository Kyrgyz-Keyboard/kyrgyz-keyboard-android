package com.example.kyrgyz_keyboard_android.keyboard.model

import com.example.kyrgyz_keyboard_android.R
import com.example.kyrgyz_keyboard_android.ui.keyboard.utils.KeyboardConstants

object SymbolsLayout1 {
    val symbolsRow1 = listOf(
        KeyUiModel(ch = "1"), KeyUiModel(ch = "2"), KeyUiModel(ch = "3"),
        KeyUiModel(ch = "4"), KeyUiModel(ch = "5"), KeyUiModel(ch = "6"),
        KeyUiModel(ch = "7"), KeyUiModel(ch = "8"), KeyUiModel(ch = "9"),
        KeyUiModel(ch = "0")
    )

    val symbolsRow2 = listOf(
        KeyUiModel(ch = "@"), KeyUiModel(ch = "#"), KeyUiModel(ch = "\u2286"),
        KeyUiModel(ch = "_"), KeyUiModel(ch = "&"), KeyUiModel(ch = "-"),
        KeyUiModel(ch = "+"), KeyUiModel(ch = "("), KeyUiModel(ch = ")"),
        KeyUiModel(ch = "/")
    )

    val symbolsRow3 = listOf(
        KeyUiModel(isSpecial = true, ch = "=\\<", weight = 1.4f),
        KeyUiModel(ch = "*"), KeyUiModel(ch = "\""), KeyUiModel(ch = "'"),
        KeyUiModel(ch = ":"), KeyUiModel(ch = ";"),
        KeyUiModel(ch = "!"), KeyUiModel(ch = "?"),
        KeyUiModel(isSpecial = true, img = R.drawable.ic_remove, weight = 1.4f)
    )

    fun getSymbolsRow5(isLatinLayout: Boolean) = listOf(
        KeyUiModel(isSpecial = true, ch = KeyboardConstants.ALPHA_CHARACTER, weight = 1.4f),
        KeyUiModel(ch = ","),
        KeyUiModel(ch = KeyboardConstants.getSpaceCharacter(isLatinLayout), weight = 5f),
        KeyUiModel(ch = "."),
        KeyUiModel(isSpecial = true, img = R.drawable.ic_enter, weight = 2f)
    )
}

object SymbolsLayout2 {
    val symbolsRow1 = listOf(
        KeyUiModel(ch = "~"), KeyUiModel(ch = "'"), KeyUiModel(ch = "|"),
        KeyUiModel(ch = "•"), KeyUiModel(ch = "√"), KeyUiModel(ch = "π"),
        KeyUiModel(ch = "÷"), KeyUiModel(ch = "×"), KeyUiModel(ch = "§"),
        KeyUiModel(ch = "△")
    )

    val symbolsRow2 = listOf(
        KeyUiModel(ch = "$"), KeyUiModel(ch = "€"), KeyUiModel(ch = "¥"),
        KeyUiModel(ch = "^"), KeyUiModel(ch = "<"), KeyUiModel(ch = "="),
        KeyUiModel(ch = ">"), KeyUiModel(ch = "{"), KeyUiModel(ch = "}"),
        KeyUiModel(ch = "\\")
    )


    val symbolsRow3 = listOf(
        KeyUiModel(isSpecial = true, ch = "?123", weight = 1.4f),
        KeyUiModel(ch = "%"), KeyUiModel(ch = "["), KeyUiModel(ch = "]"),
        KeyUiModel(ch = "№"), KeyUiModel(ch = "±"), KeyUiModel(ch = "©"),
        KeyUiModel(ch = "®"),
        KeyUiModel(isSpecial = true, img = R.drawable.ic_remove, weight = 1.4f)
    )

    fun getSymbolsRow5(isLatinLayout: Boolean) = listOf(
        KeyUiModel(isSpecial = true, ch = KeyboardConstants.ALPHA_CHARACTER, weight = 1.4f),
        KeyUiModel(ch = ","),
        KeyUiModel(ch = KeyboardConstants.getSpaceCharacter(isLatinLayout), weight = 5f),
        KeyUiModel(ch = "."),
        KeyUiModel(isSpecial = true, img = R.drawable.ic_enter, weight = 2f)
    )
}
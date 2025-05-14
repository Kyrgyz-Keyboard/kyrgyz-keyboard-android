package com.example.kyrgyz_keyboard_android.keyboard.model

import com.example.kyrgyz_keyboard_android.R
import com.example.kyrgyz_keyboard_android.ui.keyboard.utils.KeyboardConstants

object CyrillicUiLayout {
    val row1 = listOf(
        KeyUiModel(isSpecial = true, img = R.drawable.ic_globe),
        KeyUiModel(isSpecial = true, ch = KeyboardConstants.ENESAY_CHARACTER, weight = 2f),
        KeyUiModel(ch = "ң"),
        KeyUiModel(ch = "ө"),
        KeyUiModel(ch = "ү"),
        KeyUiModel(isSpecial = true, ch = KeyboardConstants.CYRILLIC_DICT, weight = 2f),
        KeyUiModel(isSpecial = true, ch = KeyboardConstants.LIGHT)
    )


    val row2 = listOf(
        KeyUiModel(ch = "й"),
        KeyUiModel(ch = "ц"),
        KeyUiModel(ch = "у"),
        KeyUiModel(ch = "к"),
        KeyUiModel(ch = "е"),
        KeyUiModel(ch = "н"),
        KeyUiModel(ch = "г"),
        KeyUiModel(ch = "ш"),
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
        KeyUiModel(ch = "о"),
        KeyUiModel(ch = "л"),
        KeyUiModel(ch = "д"),
        KeyUiModel(ch = "ж"),
        KeyUiModel(ch = "э")
    )

    val row4 = listOf(
        KeyUiModel(isSpecial = true, img = R.drawable.ic_caps, weight = 1.4f),
        KeyUiModel(ch = "я"),
        KeyUiModel(ch = "ч"),
        KeyUiModel(ch = "с"),
        KeyUiModel(ch = "м"),
        KeyUiModel(ch = "и"),
        KeyUiModel(ch = "т"),
        KeyUiModel(ch = "б"),
        KeyUiModel(ch = "ю"),
        KeyUiModel(isSpecial = true, img = R.drawable.ic_remove, weight = 1.4f)
    )

    val row5 = listOf(
        KeyUiModel(isSpecial = true, ch = KeyboardConstants.SYMBOLS_CHARACTER, weight = 1.4f),
        KeyUiModel(ch = ","),
        KeyUiModel(ch = KeyboardConstants.CYRILLIC_SPACE, weight = 5f),
        KeyUiModel(ch = "."),
        KeyUiModel(isSpecial = true, img = R.drawable.ic_enter, weight = 2f)
    )
}
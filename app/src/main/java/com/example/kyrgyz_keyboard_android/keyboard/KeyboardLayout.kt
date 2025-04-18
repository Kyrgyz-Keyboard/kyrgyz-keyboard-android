package com.example.kyrgyz_keyboard_android.keyboard

import com.example.kyrgyz_keyboard_android.R

data class KeyUiModel(
    val isSpecial: Boolean = false,
    val ch: String? = null,
    val img: Int? = null,
    val weight: Float = 1f,
    val isActive: Boolean = false
)

object KeyboardLayout {
    val row1 = listOf(
        KeyUiModel(ch = "й"),
        KeyUiModel(ch = "0"),
        KeyUiModel(ch = "1"),
        KeyUiModel(ch = "2"),
        KeyUiModel(ch = "3"),
        KeyUiModel(ch = "4"),
        KeyUiModel(ch = "5"),
        KeyUiModel(ch = "6"),
        KeyUiModel(ch = "7"),
        KeyUiModel(ch = "8"),
        KeyUiModel(ch = "9")
    )


    val row2 = listOf(
        KeyUiModel(ch = "ң"),
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
        KeyUiModel(isSpecial = true, img = R.drawable.ic_caps, weight = 1.5f),
        KeyUiModel(ch = "я"),
        KeyUiModel(ch = "ч"),
        KeyUiModel(ch = "с"),
        KeyUiModel(ch = "м"),
        KeyUiModel(ch = "и"),
        KeyUiModel(ch = "т"),
        KeyUiModel(ch = "ү"),
        KeyUiModel(ch = "б"),
        KeyUiModel(ch = "ю"),
        KeyUiModel(isSpecial = true, img = R.drawable.ic_remove, weight = 1.5f)
    )

    val row5 = listOf(
        KeyUiModel(isSpecial = true, img = R.drawable.ic_more, weight = 1.5f),
        KeyUiModel(ch = ","),
        KeyUiModel(ch = "аралык", weight = 5f),
        KeyUiModel(ch = ".")
    )
}
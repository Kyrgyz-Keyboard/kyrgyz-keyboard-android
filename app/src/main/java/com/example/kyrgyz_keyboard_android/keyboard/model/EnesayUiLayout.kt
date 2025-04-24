package com.example.kyrgyz_keyboard_android.keyboard.model

import com.example.kyrgyz_keyboard_android.R
import com.example.kyrgyz_keyboard_android.ui.keyboard.utils.KeyboardConstants

object EnesayUiLayout {
    val row1 = listOf(
        KeyUiModel(isSpecial = true, img = R.drawable.ic_globe),
        KeyUiModel(isSpecial = true, ch = KeyboardConstants.ENESAY_CHARACTER, weight = 2f),
        KeyUiModel(ch = "\uD803\uDC2D", hint = "ң"),
        KeyUiModel(ch = "\uD803\uDC07", hint = "ө"),
        KeyUiModel(ch = "\uD803\uDC08", hint = "ү"),
        KeyUiModel(isSpecial = true, ch = "\uD803\uDC3D\uD803\uDC07\uD803\uDC15\uD803\uDC12\uD803\uDC08\uD803\uDC34", weight = 2f),
        KeyUiModel(isSpecial = true, img = R.drawable.ic_emoji)
    )

    val row2 = listOf(
        KeyUiModel(ch = "\uD803\uDC00", hint = "а"),
        KeyUiModel(ch = "\uD803\uDC02", hint = "э"),
        KeyUiModel(ch = "\uD803\uDC09", hint = "б"),
        KeyUiModel(ch = "\uD803\uDC0A", hint = "в"),
        KeyUiModel(ch = "\uD803\uDC0D", hint = "г"),
        KeyUiModel(ch = "\uD803\uDC12", hint = "д"),
        KeyUiModel(ch = "\uD803\uDC05", hint = "е"),
        KeyUiModel(ch = "\uD803\uDC33", hint = "ж"),
        KeyUiModel(ch = "\uD803\uDC15", hint = "з"),
        KeyUiModel(ch = "\uD803\uDC04", hint = "и"),
        KeyUiModel(ch = "\uD803\uDC16", hint = "й"),
        KeyUiModel(ch = "\uD803\uDC34", hint = "к")
    )

    val row3 = listOf(
        KeyUiModel(ch = "\uD803\uDC36", hint = "кы"),
        KeyUiModel(ch = "\uD803\uDC38", hint = "ко|ку"),
        KeyUiModel(ch = "\uD803\uDC1C", hint = "кө|кү"),
        KeyUiModel(ch = "\uD803\uDC1E", hint = "л"),
        KeyUiModel(ch = "\uD803\uDC21", hint = "лт"),
        KeyUiModel(ch = "\uD803\uDC22", hint = "м"),
        KeyUiModel(ch = "\uD803\uDC23", hint = "н"),
        KeyUiModel(ch = "\uD803\uDC27", hint = "нт"),
        KeyUiModel(ch = "\uD803\uDC29", hint = "нч"),
        KeyUiModel(ch = "\uD803\uDC2A", hint = "нь"),
        KeyUiModel(ch = "\uD803\uDC06", hint = "о|у")
    )

    val row4 = listOf(
        KeyUiModel(isSpecial = true, img = R.drawable.ic_caps, weight = 1.4f),
        KeyUiModel(ch = "\uD803\uDC2F", hint = "п"),
        KeyUiModel(ch = "\uD803\uDC3B", hint = "р"),
        KeyUiModel(ch = "\uD803\uDC3D", hint = "с"),
        KeyUiModel(ch = "\uD803\uDC44", hint = "т"),
        KeyUiModel(ch = "\uD803\uDC47", hint = "то"),
        KeyUiModel(ch = "\uD803\uDC32", hint = "ч"),
        KeyUiModel(ch = "\uD803\uDC31", hint = "чи"),
        KeyUiModel(ch = "\uD803\uDC41", hint = "ш"),
        KeyUiModel(ch = "\uD803\uDC03", hint = "ы"),
        KeyUiModel(isSpecial = true, img = R.drawable.ic_remove, weight = 1.4f)
    )

    val row5 = listOf(
        KeyUiModel(isSpecial = true, ch = KeyboardConstants.SYMBOLS_CHARACTER, weight = 1.4f),
        KeyUiModel(ch = ","),
        KeyUiModel(ch = KeyboardConstants.ENESAY_SPACE, weight = 5f),
        KeyUiModel(ch = "."),
        KeyUiModel(isSpecial = true, img = R.drawable.ic_enter, weight = 2f)
    )

}
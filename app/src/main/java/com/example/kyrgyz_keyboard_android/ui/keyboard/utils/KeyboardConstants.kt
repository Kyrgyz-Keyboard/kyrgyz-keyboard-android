package com.example.kyrgyz_keyboard_android.ui.keyboard.utils

object KeyboardConstants {
    // Backspace handling
    const val INITIAL_DELETE_SPEED = 200L
    const val MIN_DELETE_SPEED = 20L
    const val SPEED_DECREASE_FACTOR_INITIAL = 0.7f
    const val SPEED_DECREASE_FACTOR_NORMAL = 0.9f
    const val INITIAL_SPEED_PHASE_ITERATIONS = 5

    // Special Characters
    const val CYRILLIC_DICT = "сөздүк"
    const val LATIN_DICT = "sözdük"
    const val ENESAY_DICT = "\uD803\uDC3D\uD803\uDC07\uD803\uDC15\uD803\uDC12\uD803\uDC08\uD803\uDC34"
    const val CYRILLIC_SPACE = "аралык"
    const val LATIN_SPACE = "aralyk"
    const val ENESAY_SPACE = "\uD803\uDC00\uD803\uDC3B\uD803\uDC00\uD803\uDC1E\uD803\uDC03\uD803\uDC34"
    const val SYMBOLS_CHARACTER = "123"
    const val ALPHA_CHARACTER = "абв"
    const val ENESAY_CHARACTER = "\uD803\uDC36\uD803\uDC3B\uD803\uDC0D\uD803\uDC03\uD803\uDC15"

    const val LIGHT = "\uD83C\uDF18"
//    const val LIGHT = "\uD83C\uDF11"
//    const val DARK = "\uD83C\uDF15"
    const val DARK = "\uD83C\uDF16"

    // Dictionary Categories
    const val COMMON_DICT = "Жалпы"
    const val MONTH_DAY_DICT = "Айлар"
    const val DAYS_DICT = "Жума"
    const val FUN_DICT = "Чаташтырба"

    fun getSpaceCharacter(isLatinLayout: Boolean): String {
        return if (isLatinLayout) LATIN_SPACE else CYRILLIC_SPACE
    }
}
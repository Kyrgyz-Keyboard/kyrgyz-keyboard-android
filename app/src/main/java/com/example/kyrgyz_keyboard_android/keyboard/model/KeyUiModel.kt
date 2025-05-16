package com.example.kyrgyz_keyboard_android.keyboard.model

data class KeyUiModel(
    val isSpecial: Boolean = false,
    val ch: String? = null,
    val hint: String? = null,
    val img: Int? = null,
    val weight: Float = 1f,
    var isActive: CapsLockState = CapsLockState.TEMPORARY
)


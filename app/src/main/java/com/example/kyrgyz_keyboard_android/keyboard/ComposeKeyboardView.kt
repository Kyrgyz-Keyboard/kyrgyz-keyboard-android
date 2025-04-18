package com.example.kyrgyz_keyboard_android.keyboard

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView
import com.example.kyrgyz_keyboard_android.ui.HomeScreen

class ComposeKeyboardView(context: Context) : AbstractComposeView(context) {

    @Composable
    override fun Content() {
        HomeScreen()
    }
}
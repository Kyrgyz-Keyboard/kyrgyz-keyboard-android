package com.example.kyrgyz_keyboard_android.keyboard.service

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kyrgyz_keyboard_android.keyboard.viewmodel.KeyboardViewModel
import com.example.kyrgyz_keyboard_android.keyboard.viewmodel.KeyboardViewModelFactory
import com.example.kyrgyz_keyboard_android.ui.keyboard.HomeScreen

class ComposeKeyboardView(context: Context) : AbstractComposeView(context) {
    @Composable
    override fun Content() {
        val factory = KeyboardViewModelFactory(context.applicationContext as android.app.Application)
        val viewModel: KeyboardViewModel = viewModel(factory = factory)
        HomeScreen(viewModel = viewModel)
    }
}
package com.example.kyrgyz_keyboard_android.keyboard.service

import android.view.View
import android.view.inputmethod.InputConnection
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.kyrgyz_keyboard_android.R
import com.example.kyrgyz_keyboard_android.keyboard.service.ComposeKeyboardView
import com.example.kyrgyz_keyboard_android.keyboard.service.LifecycleInputMethodService

class KyrgyzKeyboardIME() : LifecycleInputMethodService(), ViewModelStoreOwner,
    SavedStateRegistryOwner {
    override fun onCreateInputView(): View {
        val view = ComposeKeyboardView(this)

        window?.window?.decorView?.let { decorView ->
            decorView.setViewTreeLifecycleOwner(this)
            decorView.setViewTreeViewModelStoreOwner(this)
            decorView.setViewTreeSavedStateRegistryOwner(this)
        }
        return view

    }

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)

    }

    override val viewModelStore: ViewModelStore
        get() = store
    override val lifecycle: Lifecycle
        get() = dispatcher.lifecycle

    private val store = ViewModelStore()

    private val savedStateRegistryController = SavedStateRegistryController.Companion.create(this)

    override val savedStateRegistry: SavedStateRegistry get() = savedStateRegistryController.savedStateRegistry

    private fun handleKeyPress(key: String) {
        val inputConnection: InputConnection? = currentInputConnection
        when (key) {
            R.drawable.ic_remove.toString() -> inputConnection?.deleteSurroundingText(1, 0)
            " " -> inputConnection?.commitText(" ", 1)
            else -> inputConnection?.commitText(key, 1)
        }
    }

}
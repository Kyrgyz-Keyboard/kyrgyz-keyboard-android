package com.example.kyrgyz_keyboard_android.keyboard.service

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.kyrgyz_keyboard_android.keyboard.viewmodel.KeyboardViewModel
import com.example.kyrgyz_keyboard_android.keyboard.viewmodel.KeyboardViewModelFactory

class KyrgyzKeyboardIME() : LifecycleInputMethodService(), ViewModelStoreOwner,
    SavedStateRegistryOwner {
    
    private lateinit var viewModel: KeyboardViewModel
    
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
        
        val factory = KeyboardViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[KeyboardViewModel::class.java]
    }

    override val viewModelStore: ViewModelStore
        get() = store
    override val lifecycle: Lifecycle
        get() = dispatcher.lifecycle

    private val store = ViewModelStore()

    private val savedStateRegistryController = SavedStateRegistryController.Companion.create(this)

    override val savedStateRegistry: SavedStateRegistry get() = savedStateRegistryController.savedStateRegistry

    override fun onStartInput(attribute: EditorInfo, restarting: Boolean) {
        super.onStartInput(attribute, restarting)
        
        if (!restarting && ::viewModel.isInitialized) {
            viewModel.resetState()
        }
    }
    
    private var currentPackageName: String? = null
    
    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        
        val packageName = info?.packageName?.toString()
        
        if (packageName != null && packageName != currentPackageName && ::viewModel.isInitialized) {
            viewModel.resetState()
            currentPackageName = packageName
        }
    }
}
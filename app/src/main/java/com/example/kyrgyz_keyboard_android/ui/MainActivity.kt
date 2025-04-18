package com.example.kyrgyz_keyboard_android.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KeyboardSettingsScreen()
        }
    }
}

@Composable
fun KeyboardSettingsScreen() {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), verticalArrangement = Arrangement.Center
    ) {
        Text("Enable and select the Kyrgyz Keyboard", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { openKeyboardSettings(context) }) {
            Text("Enable Keyboard")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = { switchToCustomKeyboard(context) }) {
            Text("Switch to Kyrgyz Keyboard")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Test your keyboard below:", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Type here...") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { hideKeyboard(context) }),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

fun openKeyboardSettings(context: Context) {
    val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    ContextCompat.startActivity(context, intent, null)
}

fun switchToCustomKeyboard(context: Context) {
    val imeManager = context.getSystemService(InputMethodManager::class.java)
    imeManager?.showInputMethodPicker()
}

fun hideKeyboard(context: Context) {
    val imeManager = context.getSystemService(InputMethodManager::class.java)
    imeManager?.hideSoftInputFromWindow(null, 0)
}


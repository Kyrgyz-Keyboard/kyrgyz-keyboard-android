package com.example.kyrgyz_keyboard_android.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions
import com.example.kyrgyz_keyboard_android.ui.theme.Dimensions.keyTextSize
import com.example.kyrgyz_keyboard_android.ui.theme.KyrgyzKeyboardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KyrgyzKeyboardTheme {
                KeyboardSettingsScreen()
            }
        }
    }
}

@Composable
fun KeyboardSettingsScreen() {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var isKeyboardEnabled by remember { mutableStateOf(false) }

    fun checkKeyboardStatus() {
        val imeManager = context.getSystemService(InputMethodManager::class.java)
        isKeyboardEnabled = imeManager?.enabledInputMethodList?.any {
            it.packageName == context.packageName
        } == true
    }

    LaunchedEffect(Unit) {
        checkKeyboardStatus()
        if (!isKeyboardEnabled) {
            showDialog = true
        }
    }

    if (showDialog && !isKeyboardEnabled) {
        AlertDialog(onDismissRequest = {
            checkKeyboardStatus()
            if (isKeyboardEnabled) {
                showDialog = false
            }
        }, title = {
            Text("Баскычтопту иштетүү")
        }, text = {
            Text("Кыргыз баскычтобун колдонуу үчүн уруксат бериңиз")
        }, confirmButton = {
            Button(onClick = {
                openKeyboardSettings(context)
                showDialog = false
            }) {
                Text("Уруксат берүү")
            }
        }, dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                ), onClick = {
                    checkKeyboardStatus()
                    showDialog = false
                }) {
                Text("Жабуу")
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Арыба!\nЖаңы баскычтопту кармап көр!",
            fontSize = keyTextSize,
            modifier = Modifier.padding(top = 150.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("Сынап көрүңүз:", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Бул жерге жазыңыз...") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { hideKeyboard(context) }),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .weight(1f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier
                .padding(bottom = 350.dp)
                .fillMaxWidth(),
            onClick = { switchToCustomKeyboard(context) },
            shape = RoundedCornerShape(Dimensions.keyCornerRadius)
        ) {
            Text("Кыргыз баскычтоптусуна өтүү")
        }
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


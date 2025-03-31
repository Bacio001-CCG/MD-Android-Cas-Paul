package com.example.md_android_cas_paul.ui.settings

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit

@Composable
fun SettingsScreen(onSave: () -> Unit) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    val initialLimit = prefs.getInt("searchLimit", 10) // Default 10
    var searchLimit by remember { mutableStateOf(initialLimit.toFloat()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Search Limit: ${searchLimit.toInt()}",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Slider(
            value = searchLimit,
            onValueChange = { searchLimit = it },
            valueRange = 1f..50f,
            steps = 48,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Button(
            onClick = {
                prefs.edit {
                    putInt("searchLimit", searchLimit.toInt())
                }
                onSave()
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Save")
        }
    }
}
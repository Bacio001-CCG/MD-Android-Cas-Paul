package com.example.md_android_cas_paul.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BookDetailScreen(bookTitle: String, bookKey: String) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp).statusBarsPadding()) {
        Text(
            text = "Details for: $bookTitle\nkey: $bookKey\n(More features coming soon!)",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxSize()
        )
    }
}

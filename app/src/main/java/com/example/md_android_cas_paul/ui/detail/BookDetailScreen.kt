package com.example.md_android_cas_paul.ui.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun BookDetailScreen(bookTitle: String) {
    Text(
        text = "Details for: $bookTitle\n(More features coming soon!)",
        fontSize = 20.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxSize()
    )
}

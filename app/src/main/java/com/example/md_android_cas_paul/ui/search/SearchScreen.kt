package com.example.md_android_cas_paul.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.md_android_cas_paul.data.model.Book
import com.example.md_android_cas_paul.viewmodel.BookViewModel

@Composable
fun SearchScreen(viewModel: BookViewModel, onBookSelected: (Book) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = viewModel.searchQuery.value,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            label = { Text("Search books") },
            modifier = Modifier.fillMaxWidth()
        )
        when {
            viewModel.isLoading.value -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp))
            }
            viewModel.error.value != null -> {
                Text(
                    text = viewModel.error.value ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            else -> {
                LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                    items(viewModel.searchResults.value) { book ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            onClick = { onBookSelected(book) }
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(text = book.title)
                                Text(text = "by ${book.author}")
                            }
                        }
                    }
                }
            }
        }
    }
}

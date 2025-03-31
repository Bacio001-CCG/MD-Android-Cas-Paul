package com.example.md_android_cas_paul.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.md_android_cas_paul.data.model.Book
import com.example.md_android_cas_paul.viewmodel.SearchBookViewModel

@Composable
fun SearchScreen(viewModel: SearchBookViewModel, onBookSelected: (Book) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp).statusBarsPadding()) {
        TextField(
            value = viewModel.searchQuery.value,
            onValueChange = { viewModel.searchQuery.value = it },
            label = { Text("Search books") },
            modifier = Modifier
                .fillMaxWidth()
                .onKeyEvent { event ->
                    if (event.key == Key.Enter) {
                        viewModel.performSearch()
                        true
                    } else {
                        false
                    }
                },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { viewModel.performSearch() }
            )

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

package com.example.md_android_cas_paul.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.md_android_cas_paul.data.model.Book
import com.example.md_android_cas_paul.data.repository.BookRepository

class SearchBookViewModel(private val repository: BookRepository) : ViewModel() {val searchQuery = mutableStateOf("")
    val searchResults = mutableStateOf<List<Book>>(emptyList())
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
        if (query.isNotEmpty()) {
            searchBooks(query)
        }
    }

    private fun searchBooks(query: String) {
        isLoading.value = true
        repository.searchBooks(
            query,
            onSuccess = { books ->
                searchResults.value = books
                isLoading.value = false
                error.value = null
            },
            onError = { exception ->
                error.value = exception.message ?: "Failed to search books"
                isLoading.value = false
            }
        )
    }
}

package com.example.md_android_cas_paul.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.md_android_cas_paul.data.model.Book
import com.example.md_android_cas_paul.data.repository.BookRepository
import kotlinx.coroutines.launch

class SearchBookViewModel(private val repository: BookRepository) : ViewModel() {
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _searchResults = mutableStateOf<List<Book>>(emptyList())
    val searchResults: State<List<Book>> = _searchResults

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        if (query.isNotEmpty()) {
            searchBooks(query)
        } else {
            _searchResults.value = emptyList()
            _error.value = null
        }
    }

    private fun searchBooks(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _searchResults.value = repository.searchBooks(query)
            } catch (e: Exception) {
                _error.value = "Failed to load books: ${e.message}"
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}

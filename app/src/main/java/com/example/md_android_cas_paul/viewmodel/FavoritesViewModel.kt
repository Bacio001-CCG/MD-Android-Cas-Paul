package com.example.md_android_cas_paul.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.md_android_cas_paul.data.model.Book
import com.example.md_android_cas_paul.data.repository.BookRepository

class FavoritesViewModel(private val repository: BookRepository) : ViewModel() {
    val favorites = mutableStateOf<List<Book>>(emptyList())

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        favorites.value = repository.getAllFavorites()
    }

    fun refreshFavorites() {
        loadFavorites()
    }
}
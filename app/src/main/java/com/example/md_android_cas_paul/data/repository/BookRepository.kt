package com.example.md_android_cas_paul.data.repository

import android.content.Context
import com.example.md_android_cas_paul.data.local.FavoritesDbHelper
import com.example.md_android_cas_paul.data.model.Book
import com.example.md_android_cas_paul.data.remote.OpenLibraryApi

class BookRepository(context: Context) {
    private val api = OpenLibraryApi(context)
    private val favoritesDbHelper = FavoritesDbHelper(context)

    suspend fun searchBooks(query: String): List<Book> {
        val response = api.searchBooks(query)
        return response.map { doc ->
            Book(workKey = doc.key, title = doc.title, author = doc.author_name?.firstOrNull() ?: "Unknown")
        }
    }

    fun addFavorite(workKey: String, title: String, author: String?) {
        favoritesDbHelper.addFavorite(workKey, title, author)
    }

    fun removeFavorite(workKey: String) {
        favoritesDbHelper.removeFavorite(workKey)
    }

    fun isFavorite(workKey: String): Boolean {
        return favoritesDbHelper.isFavorite(workKey)
    }

    fun getAllFavorites(): List<Book> {
        return favoritesDbHelper.getAllFavorites().map { (workKey, title, author) ->
            Book(workKey, title, author)
        }
    }
}
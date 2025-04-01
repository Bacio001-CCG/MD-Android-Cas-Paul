package com.example.md_android_cas_paul.data.repository

import android.content.Context
import com.example.md_android_cas_paul.data.local.FavoritesDbHelper
import com.example.md_android_cas_paul.data.model.Book
import com.example.md_android_cas_paul.data.remote.OpenLibraryApi

class BookRepository(context: Context) {
    private val api = OpenLibraryApi(context)
    private val favoritesDbHelper = FavoritesDbHelper(context)

    fun searchBooks(query: String, onSuccess: (List<Book>) -> Unit, onError: (Exception) -> Unit) {
        api.searchBooks(
            query,
            onSuccess = { bookDocs ->
                val books = bookDocs.map { doc ->
                    Book(workKey = doc.key, title = doc.title, author = doc.author_name?.firstOrNull() ?: "Unknown")
                }
                onSuccess(books)
            },
            onError = { error ->
                onError(error)
            }
        )
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
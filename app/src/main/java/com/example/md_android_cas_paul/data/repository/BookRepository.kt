package com.example.md_android_cas_paul.data.repository

import android.content.Context
import com.example.md_android_cas_paul.data.model.Book
import com.example.md_android_cas_paul.data.remote.OpenLibraryApi

class BookRepository(context: Context) {
    private val api = OpenLibraryApi(context)

    suspend fun searchBooks(query: String): List<Book> {
        val response = api.searchBooks(query)
        return response.map { doc ->
            Book(title = doc.title, author = doc.author_name?.firstOrNull() ?: "Unknown")
        }
    }
}
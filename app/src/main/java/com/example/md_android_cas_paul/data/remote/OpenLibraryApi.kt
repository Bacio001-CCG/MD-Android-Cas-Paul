package com.example.md_android_cas_paul.data.remote

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class OpenLibraryApi(private val context: Context) {
    suspend fun searchBooks(query: String): List<BookDoc> = suspendCoroutine { continuation ->
        val url = "https://openlibrary.org/search.json?q=${query.replace(" ", "+")}"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val docs = response.getJSONArray("docs")
                val books = mutableListOf<BookDoc>()
                for (i in 0 until docs.length()) {
                    val doc = docs.getJSONObject(i)
                    val authors = doc.optJSONArray("author_name")?.let { array ->
                        (0 until array.length()).map { array.getString(it) }
                    }
                    books.add(BookDoc(doc.getString("title"), authors))
                }
                continuation.resume(books)
            },
            { error ->
                continuation.resumeWithException(error)
            }
        )
        Volley.newRequestQueue(context).add(request)
    }
}

data class BookDoc(val title: String, val author_name: List<String>?)
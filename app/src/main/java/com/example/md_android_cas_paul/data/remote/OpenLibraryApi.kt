package com.example.md_android_cas_paul.data.remote

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class OpenLibraryApi(private val context: Context) {
    fun searchBooks(query: String, onSuccess: (List<BookDoc>) -> Unit, onError: (Exception) -> Unit) {
        val limit = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).getInt("searchLimit", 25)
        val url = "https://openlibrary.org/search.json?q=${query.replace(" ", "+")}&limit=$limit"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val docs = response.getJSONArray("docs")
                    val books = mutableListOf<BookDoc>()
                    for (i in 0 until docs.length()) {
                        val doc = docs.getJSONObject(i)
                        val authors = doc.optJSONArray("author_name")?.let { array ->
                            (0 until array.length()).map { array.getString(it) }
                        }
                        books.add(BookDoc(doc.getString("key"), doc.getString("title"), authors))
                    }
                    onSuccess(books)
                } catch (e: JSONException) {
                    onError(e)
                }
            },
            { error ->
                onError(error)
            }
        )
        Volley.newRequestQueue(context).add(request)
    }
}

data class BookDoc(val key: String, val title: String, val author_name: List<String>?)
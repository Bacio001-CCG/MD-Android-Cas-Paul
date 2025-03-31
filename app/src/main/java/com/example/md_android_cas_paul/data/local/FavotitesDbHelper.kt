package com.example.md_android_cas_paul.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FavoritesDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "Favorites.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_FAVORITES = "favorites"
        private const val COLUMN_WORK_KEY = "workKey"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_AUTHOR = "author"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_FAVORITES (
                $COLUMN_WORK_KEY TEXT PRIMARY KEY,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_AUTHOR TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITES")
        onCreate(db)
    }

    fun addFavorite(workKey: String, title: String, author: String?) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_WORK_KEY, workKey)
            put(COLUMN_TITLE, title)
            put(COLUMN_AUTHOR, author)
        }
        db.insert(TABLE_FAVORITES, null, values)
        db.close()
    }

    fun removeFavorite(workKey: String) {
        val db = writableDatabase
        db.delete(TABLE_FAVORITES, "$COLUMN_WORK_KEY = ?", arrayOf(workKey))
        db.close()
    }

    fun isFavorite(workKey: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_FAVORITES,
            arrayOf(COLUMN_WORK_KEY),
            "$COLUMN_WORK_KEY = ?",
            arrayOf(workKey),
            null,
            null,
            null
        )
        val exists = cursor.use { it.moveToFirst() }
        db.close()
        return exists
    }

    fun getAllFavorites(): List<Triple<String, String, String?>> {
        val favorites = mutableListOf<Triple<String, String, String?>>()
        val db = readableDatabase
        val cursor = db.query(TABLE_FAVORITES, null, null, null, null, null, null)
        cursor.use {
            while (it.moveToNext()) {
                val workKey = it.getString(it.getColumnIndexOrThrow(COLUMN_WORK_KEY))
                val title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE))
                val author = it.getString(it.getColumnIndexOrThrow(COLUMN_AUTHOR))
                favorites.add(Triple(workKey, title, author))
            }
        }
        db.close()
        return favorites
    }
}
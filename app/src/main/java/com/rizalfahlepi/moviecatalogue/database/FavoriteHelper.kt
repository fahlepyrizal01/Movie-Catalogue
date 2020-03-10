package com.rizalfahlepi.moviecatalogue.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns._ID
import com.rizalfahlepi.moviecatalogue.database.DatabaseContract.FavoriteColumns.Companion.ID_MOVIE
import com.rizalfahlepi.moviecatalogue.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME

class FavoriteHelper(context: Context) {
    private val dataBaseHelper: DatabaseHelper = DatabaseHelper(context)

    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavoriteHelper? = null

        fun getInstance(context: Context): FavoriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteHelper(context)
            }
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC",
            null)
    }

    fun queryById(id: Int): Cursor {
        return database.query(DATABASE_TABLE, null, "$ID_MOVIE LIKE ?", arrayOf(id.toString()), null, null, "$_ID ASC", null)
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteById(id: Int): Int {
        return database.delete(TABLE_NAME, "$ID_MOVIE = '$id'", null)
    }
}
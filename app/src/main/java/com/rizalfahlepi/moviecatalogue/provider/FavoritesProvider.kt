package com.rizalfahlepi.moviecatalogue.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.rizalfahlepi.moviecatalogue.database.DatabaseContract.AUTHORITY
import com.rizalfahlepi.moviecatalogue.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.rizalfahlepi.moviecatalogue.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.rizalfahlepi.moviecatalogue.database.FavoriteHelper

class FavoritesProvider : ContentProvider() {

    companion object {
        private const val MOVIE = 1
        private const val MOVIE_ID = 2
        private lateinit var movieHelper: FavoriteHelper

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, MOVIE)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", MOVIE_ID)
        }
    }

    override fun onCreate(): Boolean {
        movieHelper = FavoriteHelper.getInstance(context as Context)
        movieHelper.open()
        return true
    }

    override fun query(uri: Uri, strings: Array<String>?, s: String?, strings1: Array<String>?, s1: String?): Cursor? {
        return when (sUriMatcher.match(uri)) {
            MOVIE -> movieHelper.queryAll()
            MOVIE_ID -> movieHelper.queryById(uri.lastPathSegment?.toInt() ?: 0)
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (MOVIE) {
            sUriMatcher.match(uri) -> movieHelper.insert(contentValues)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(uri: Uri, contentValues: ContentValues?, s: String?, strings: Array<String>?): Int = 0

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        val deleted: Int = when (MOVIE_ID) {
            sUriMatcher.match(uri) -> movieHelper.deleteById(uri.lastPathSegment?.toInt() ?: 0)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }
}
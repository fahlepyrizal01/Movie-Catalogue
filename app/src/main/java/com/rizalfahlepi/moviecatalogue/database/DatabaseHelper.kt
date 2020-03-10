package com.rizalfahlepi.moviecatalogue.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "dbfavorite"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE = "CREATE TABLE ${DatabaseContract.FavoriteColumns.TABLE_NAME}" +
                " (${_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.FavoriteColumns.ID_MOVIE} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.TITLE} TEXT NULL," +
                " ${DatabaseContract.FavoriteColumns.NAME} TEXT NULL," +
                " ${DatabaseContract.FavoriteColumns.RELEASE_DATE} TEXT NULL," +
                " ${DatabaseContract.FavoriteColumns.FIRST_AIR_DATE} TEXT NULL," +
                " ${DatabaseContract.FavoriteColumns.RUNTIME} INT NULL," +
                " ${DatabaseContract.FavoriteColumns.EPISODE_RUNTIME} INTEGER NULL," +
                " ${DatabaseContract.FavoriteColumns.POPULARITY} REAL NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.VOTE_COUNT} INT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.POSTER_PATH} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.ORIGINAL_LANGUAGE} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.VOTE_AVERAGE} REAL NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.OVERVIEW} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.TYPE} INT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $DatabaseContract.FavoriteColumns.TABLE_NAME")
        onCreate(db)
    }
}
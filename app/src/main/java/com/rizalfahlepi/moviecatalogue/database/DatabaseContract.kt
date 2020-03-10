package com.rizalfahlepi.moviecatalogue.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.rizalfahlepi.moviecatalogue"
    const val SCHEME = "content"

    class FavoriteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "table_favorite"
            const val TITLE = "title"
            const val NAME = "name"
            const val RELEASE_DATE = "release_date"
            const val FIRST_AIR_DATE = "first_air_date"
            const val RUNTIME = "runtime"
            const val EPISODE_RUNTIME = "episode_runtime"
            const val POPULARITY = "popularity"
            const val VOTE_COUNT = "vote_count"
            const val POSTER_PATH = "poster_path"
            const val ID_MOVIE = "id_movie"
            const val ORIGINAL_LANGUAGE = "original_language"
            const val VOTE_AVERAGE = "vote_average"
            const val OVERVIEW = "overview"
            const val TYPE = "type"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}
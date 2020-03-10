package com.rizalfahlepi.myfavoritemovies.utils

import android.database.Cursor
import android.provider.BaseColumns._ID
import com.rizalfahlepi.myfavoritemovies.database.DatabaseContract
import com.rizalfahlepi.myfavoritemovies.model.MovieModel
import java.util.*

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<MovieModel> {
        val movies = ArrayList<MovieModel>()
        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val movieID = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.ID_MOVIE))
                val title = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.TITLE))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.NAME))
                val releaseDate = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.RELEASE_DATE))
                val firstAirDate = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FIRST_AIR_DATE))
                val runtime = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.RUNTIME))
                val episodeRuntime = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.POPULARITY))
                val popularity = getDouble(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.EPISODE_RUNTIME))
                val voteCount = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.VOTE_COUNT))
                val posterPath = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.POSTER_PATH))
                val originalLanguage = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.ORIGINAL_LANGUAGE))
                val voteAverage = getDouble(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.VOTE_AVERAGE))
                val overview = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.OVERVIEW))
                val type = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.TYPE))
                movies.add(
                    MovieModel(
                        id,
                        movieID,
                        title,
                        name,
                        releaseDate,
                        firstAirDate,
                        runtime,
                        episodeRuntime,
                        popularity,
                        voteCount,
                        posterPath,
                        originalLanguage,
                        voteAverage,
                        overview,
                        type
                    )
                )
            }
        }
        return movies
    }
}
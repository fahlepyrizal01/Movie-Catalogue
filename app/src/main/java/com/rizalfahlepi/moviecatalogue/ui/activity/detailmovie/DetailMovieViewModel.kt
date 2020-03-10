package com.rizalfahlepi.moviecatalogue.ui.activity.detailmovie

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.rizalfahlepi.moviecatalogue.R
import com.rizalfahlepi.moviecatalogue.api.TheMovieDBApi
import com.rizalfahlepi.moviecatalogue.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.rizalfahlepi.moviecatalogue.database.DatabaseContract.FavoriteColumns.Companion.EPISODE_RUNTIME
import com.rizalfahlepi.moviecatalogue.database.DatabaseContract.FavoriteColumns.Companion.FIRST_AIR_DATE
import com.rizalfahlepi.moviecatalogue.database.DatabaseContract.FavoriteColumns.Companion.ID_MOVIE
import com.rizalfahlepi.moviecatalogue.database.DatabaseContract.FavoriteColumns.Companion.NAME
import com.rizalfahlepi.moviecatalogue.database.DatabaseContract.FavoriteColumns.Companion.ORIGINAL_LANGUAGE
import com.rizalfahlepi.moviecatalogue.database.DatabaseContract.FavoriteColumns.Companion.OVERVIEW
import com.rizalfahlepi.moviecatalogue.database.DatabaseContract.FavoriteColumns.Companion.POPULARITY
import com.rizalfahlepi.moviecatalogue.database.DatabaseContract.FavoriteColumns.Companion.POSTER_PATH
import com.rizalfahlepi.moviecatalogue.database.DatabaseContract.FavoriteColumns.Companion.RELEASE_DATE
import com.rizalfahlepi.moviecatalogue.database.DatabaseContract.FavoriteColumns.Companion.RUNTIME
import com.rizalfahlepi.moviecatalogue.database.DatabaseContract.FavoriteColumns.Companion.TITLE
import com.rizalfahlepi.moviecatalogue.database.DatabaseContract.FavoriteColumns.Companion.TYPE
import com.rizalfahlepi.moviecatalogue.database.DatabaseContract.FavoriteColumns.Companion.VOTE_AVERAGE
import com.rizalfahlepi.moviecatalogue.database.DatabaseContract.FavoriteColumns.Companion.VOTE_COUNT
import com.rizalfahlepi.moviecatalogue.model.Movie
import com.rizalfahlepi.moviecatalogue.utils.MappingHelper
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailMovieViewModel: ViewModel() {

    private val details = MutableLiveData<Movie>()
    private val isFavorite = MutableLiveData<Boolean>()

    fun setDetailMovie(movieID: Int, listener: (error: String) -> Unit) {
        val client = AsyncHttpClient()
        client.get(TheMovieDBApi.getDetailMovie(movieID), object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val movieItems = Movie()
                    movieItems.voteAverage = responseObject.getDouble("vote_average")
                    movieItems.releaseDate = responseObject.getString("release_date")
                    movieItems.overview = responseObject.getString("overview")
                    movieItems.title = responseObject.getString("title")
                    movieItems.originalLanguage = responseObject.getString("original_language")
                    movieItems.popularity = responseObject.getDouble("popularity")
                    movieItems.voteCount = responseObject.getInt("vote_count")
                    movieItems.posterPath = responseObject.getString("poster_path")
                    movieItems.id = responseObject.getInt("id")
                    movieItems.runtime = responseObject.getInt("runtime")

                    details.postValue(movieItems)
                } catch (e: Exception) {
                    listener.invoke(e.message ?: "Something Wrong")
                    e.printStackTrace()
                }

            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray?, error: Throwable?) {
                listener.invoke(
                    when (statusCode) {
                        401 -> "$statusCode : Bad Request"
                        403 -> "$statusCode : Forbidden"
                        404 -> "$statusCode : Not Found"
                        else -> "$statusCode : ${error?.message}"
                    }
                )
            }
        })
    }

    fun getDetailMovie(): LiveData<Movie> {
        return details
    }

    fun setDetailTVSeries(movieID: Int, listener: (error: String) -> Unit) {
        val client = AsyncHttpClient()
        client.get(TheMovieDBApi.getDetailTVSeries(movieID), object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)

                    val movieItems = Movie()
                    movieItems.voteAverage = responseObject.getDouble("vote_average")
                    movieItems.firstAirDate = responseObject.getString("first_air_date")
                    movieItems.overview = responseObject.getString("overview")
                    movieItems.name = responseObject.getString("name")
                    movieItems.originalLanguage = responseObject.getString("original_language")
                    movieItems.popularity = responseObject.getDouble("popularity")
                    movieItems.voteCount = responseObject.getInt("vote_count")
                    movieItems.posterPath = responseObject.getString("poster_path")
                    movieItems.id = responseObject.getInt("id")
                    val runTime = responseObject.getJSONArray("episode_run_time")
                    movieItems.episodeRunTime = runTime.getInt(0)

                    details.postValue(movieItems)
                } catch (e: Exception) {
                    listener.invoke(e.message ?: "Something Wrong")
                    e.printStackTrace()
                }

            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray?, error: Throwable?) {
                listener.invoke(
                    when (statusCode) {
                        401 -> "$statusCode : Bad Request"
                        403 -> "$statusCode : Forbidden"
                        404 -> "$statusCode : Not Found"
                        else -> "$statusCode : ${error?.message}"
                    }
                )
            }
        })
    }

    fun getDetailTVSeries(): LiveData<Movie> {
        return details
    }

    fun checkFavorite(movieID: Int, context: Context, listener: (error: String) -> Unit) {
        val result: Boolean
        try {
            val uriWithId = Uri.parse("$CONTENT_URI/$movieID")
            val cursor = context.contentResolver.query(uriWithId, null, null, null, null)
            if (cursor != null) {
                result = MappingHelper.mapCursorToObject(cursor)
                cursor.close()

                isFavorite.postValue(result)
            }
        }catch (e: Exception){
            listener.invoke(e.message ?: "Something Wrong")
            e.printStackTrace()
        }
    }

    fun getFavorite(): LiveData<Boolean> {
        return isFavorite
    }

    fun insertFavorite(movie: Movie, type: Int, context: Context, listener: (message: String) -> Unit) {
        try {
            val values = ContentValues()
            values.put(ID_MOVIE, movie.id)
            when(type){
                0 -> {
                    values.put(TITLE, movie.title)
                    values.put(RELEASE_DATE, movie.releaseDate)
                    values.put(RUNTIME, movie.runtime)
                    values.put(NAME, "")
                    values.put(FIRST_AIR_DATE, "")
                    values.put(EPISODE_RUNTIME, 0)
                }

                1 -> {
                    values.put(TITLE, "")
                    values.put(RELEASE_DATE, "")
                    values.put(RUNTIME, 0)
                    values.put(NAME, movie.name)
                    values.put(FIRST_AIR_DATE, movie.firstAirDate)
                    values.put(EPISODE_RUNTIME, movie.episodeRunTime)
                }
            }
            values.put(POPULARITY, movie.popularity)
            values.put(VOTE_COUNT, movie.voteCount)
            values.put(POSTER_PATH, movie.posterPath)
            values.put(ORIGINAL_LANGUAGE, movie.originalLanguage)
            values.put(VOTE_AVERAGE, movie.voteAverage)
            values.put(OVERVIEW, movie.overview)
            values.put(TYPE, type)

            context.contentResolver.insert(CONTENT_URI, values)

            listener.invoke(context.getString(R.string.text_added_to_favorite))
        }catch (e: Exception){
            listener.invoke(e.message ?: "Something Wrong")
            e.printStackTrace()
        }
    }

    fun removeFavorite(movieID: Int, context: Context, listener: (message: String) -> Unit) {
        try {
            val uriWithId = Uri.parse("$CONTENT_URI/$movieID")
            context.contentResolver.delete(uriWithId, null, null)

            listener.invoke(context.getString(R.string.text_removed_from_favorite))
        }catch (e: Exception){
            listener.invoke(e.message ?: "Something Wrong")
            e.printStackTrace()
        }
    }
}
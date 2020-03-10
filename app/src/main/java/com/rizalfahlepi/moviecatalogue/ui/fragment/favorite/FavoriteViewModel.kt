package com.rizalfahlepi.moviecatalogue.ui.fragment.favorite

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rizalfahlepi.moviecatalogue.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.rizalfahlepi.moviecatalogue.model.Movie
import com.rizalfahlepi.moviecatalogue.utils.MappingHelper

class FavoriteViewModel: ViewModel() {

    private val listMovies = MutableLiveData<ArrayList<Movie>>()
    private val listTVSeries = MutableLiveData<ArrayList<Movie>>()

    fun setMovie(context: Context?, listener: (error: String) -> Unit) {
        val listItems = ArrayList<Movie>()

        try {
            if (context != null){
                val cursor = context.contentResolver.query(CONTENT_URI, null, null, null, null)
                val movies = MappingHelper.mapCursorToArrayList(cursor)
                for (data in movies){
                    if (data.type == 0){
                        val movie = Movie()
                        movie.title = data.title
                        movie.releaseDate = data.releaseDate
                        movie.runtime = data.runtime
                        movie.popularity = data.popularity
                        movie.voteCount = data.voteCount
                        movie.posterPath = data.posterPath
                        movie.id = data.idMovie
                        movie.originalLanguage = data.originalLanguage
                        movie.voteAverage = data.voteAverage
                        movie.overview = data.overview

                        listItems.add(movie)
                    }
                }

                listMovies.postValue(listItems)
            }
        }catch (e: Exception){
            listener.invoke(e.message ?: "Something Wrong")
            e.printStackTrace()
        }
    }

    fun getMovie(): LiveData<ArrayList<Movie>> {
        return listMovies
    }

    fun setTVSeries(context: Context?, listener: (error: String) -> Unit) {
        val listItems = ArrayList<Movie>()

        try {
            if (context != null){
                val cursor = context.contentResolver.query(CONTENT_URI, null, null, null, null)
                val movies = MappingHelper.mapCursorToArrayList(cursor)
                for (data in movies){
                    if (data.type == 1){
                        val movie = Movie()
                        movie.name = data.name
                        movie.firstAirDate = data.firstAirDate
                        movie.episodeRunTime = data.episodeRunTime
                        movie.popularity = data.popularity
                        movie.voteCount = data.voteCount
                        movie.posterPath = data.posterPath
                        movie.id = data.idMovie
                        movie.originalLanguage = data.originalLanguage
                        movie.voteAverage = data.voteAverage
                        movie.overview = data.overview

                        listItems.add(movie)
                    }
                }

                cursor?.close()
                listTVSeries.postValue(listItems)
            }
        }catch (e: Exception){
            listener.invoke(e.message ?: "Something Wrong")
            e.printStackTrace()
        }
    }

    fun getTVSeries(): LiveData<ArrayList<Movie>> {
        return listTVSeries
    }
}
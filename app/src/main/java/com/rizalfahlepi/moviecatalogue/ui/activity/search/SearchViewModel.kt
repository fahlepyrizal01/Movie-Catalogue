package com.rizalfahlepi.moviecatalogue.ui.activity.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.rizalfahlepi.moviecatalogue.api.TheMovieDBApi
import com.rizalfahlepi.moviecatalogue.model.Movie
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class SearchViewModel: ViewModel() {

    private val listMovies = MutableLiveData<ArrayList<Movie>>()
    private val listTVSeries = MutableLiveData<ArrayList<Movie>>()

    fun setMovies(query : String, listener: (error: String) -> Unit) {
        val listItems = ArrayList<Movie>()

        val client = AsyncHttpClient()
        client.get(TheMovieDBApi.searchMovie(query), object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")

                    for (i in 0 until list.length()) {
                        val movie = list.getJSONObject(i)
                        val movieItems = Movie()
                        movieItems.voteAverage = movie.getDouble("vote_average")
                        movieItems.releaseDate = movie.getString("release_date")
                        movieItems.overview = movie.getString("overview")
                        movieItems.title = movie.getString("title")
                        movieItems.posterPath = movie.getString("poster_path")
                        movieItems.id = movie.getInt("id")
                        listItems.add(movieItems)
                    }

                    listMovies.postValue(listItems)
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

    fun getMovie(): LiveData<ArrayList<Movie>> {
        return listMovies
    }

    fun setTVSeries(query: String, listener: (error: String) -> Unit) {
        val listItems = ArrayList<Movie>()

        val client = AsyncHttpClient()
        client.get(TheMovieDBApi.searchTVSeries(query), object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")

                    for (i in 0 until list.length()) {
                        val movie = list.getJSONObject(i)
                        val movieItems = Movie()
                        movieItems.voteAverage = movie.getDouble("vote_average")
                        movieItems.firstAirDate = movie.getString("first_air_date")
                        movieItems.overview = movie.getString("overview")
                        movieItems.name = movie.getString("name")
                        movieItems.posterPath = movie.getString("poster_path")
                        movieItems.id = movie.getInt("id")
                        listItems.add(movieItems)
                    }

                    listTVSeries.postValue(listItems)
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

    fun getTVSeries(): LiveData<ArrayList<Movie>> {
        return listTVSeries
    }
}
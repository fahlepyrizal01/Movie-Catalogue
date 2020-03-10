package com.rizalfahlepi.myfavoritemovies.api

object TheMovieDBApi {
    fun getPosterMovie(fileName: String): String {
        return "https://image.tmdb.org/t/p/original$fileName"
    }
}
package com.rizalfahlepi.moviecatalogue.api

import com.rizalfahlepi.moviecatalogue.BuildConfig
import java.util.*


object TheMovieDBApi {
    fun getListMovie(): String {
        return "${BuildConfig.BASE_URL}3/discover/movie?api_key=${BuildConfig.TMDB_API_KEY}&language=${if (Locale.getDefault().displayLanguage.contains("Indonesia",true)) "id-ID" else "en-US"}"
    }

    fun getListTVSeries(): String {
        return "${BuildConfig.BASE_URL}3/discover/tv?api_key=${BuildConfig.TMDB_API_KEY}&language=${if (Locale.getDefault().displayLanguage.contains("Indonesia",true)) "id-ID" else "en-US"}"
    }

    fun getDetailMovie(movieID: Int): String {
        return "${BuildConfig.BASE_URL}3/movie/$movieID?api_key=${BuildConfig.TMDB_API_KEY}&language=${if (Locale.getDefault().displayLanguage.contains("Indonesia",true)) "id-ID" else "en-US"}"
    }

    fun getDetailTVSeries(movieID: Int): String {
        return "${BuildConfig.BASE_URL}3/tv/$movieID?api_key=${BuildConfig.TMDB_API_KEY}&language=${if (Locale.getDefault().displayLanguage.contains("Indonesia",true)) "id-ID" else "en-US"}"
    }

    fun getPosterMovie(fileName: String): String {
        return "${BuildConfig.IMAGE_URL}t/p/original$fileName"
    }

    fun searchMovie(query: String): String {
        return "${BuildConfig.BASE_URL}3/search/movie?api_key=${BuildConfig.TMDB_API_KEY}&language=${if (Locale.getDefault().displayLanguage.contains("Indonesia",true)) "id-ID" else "en-US"}&query=$query"
    }

    fun searchTVSeries(query: String): String {
        return "${BuildConfig.BASE_URL}3/search/tv?api_key=${BuildConfig.TMDB_API_KEY}&language=${if (Locale.getDefault().displayLanguage.contains("Indonesia",true)) "id-ID" else "en-US"}&query=$query"
    }

    fun get(query: String): String {
        return "${BuildConfig.BASE_URL}3/search/tv?api_key=${BuildConfig.TMDB_API_KEY}&language=${if (Locale.getDefault().displayLanguage.contains("Indonesia",true)) "id-ID" else "en-US"}&query=$query"
    }

    fun getReleaseMovies(date: String): String {
        return "${BuildConfig.BASE_URL}3/discover/movie?api_key=${BuildConfig.TMDB_API_KEY}&language=${if (Locale.getDefault().displayLanguage.contains("Indonesia",true)) "id-ID" else "en-US"}&primary_release_date.gte=$date&primary_release_date.lte=$date"
    }

    fun getReleaseTVSeries(date: String): String {
        return "${BuildConfig.BASE_URL}3/discover/tv?api_key=${BuildConfig.TMDB_API_KEY}&language=${if (Locale.getDefault().displayLanguage.contains("Indonesia",true)) "id-ID" else "en-US"}&primary_release_date.gte=$date&primary_release_date.lte=$date"
    }
}
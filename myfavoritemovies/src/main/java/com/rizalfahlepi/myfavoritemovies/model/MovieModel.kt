package com.rizalfahlepi.myfavoritemovies.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieModel (
    var id: Int = 0,
    var idMovie: Int? = null,
    var title: String? = null,
    var name: String? = null,
    var releaseDate: String? = null,
    var firstAirDate: String? = null,
    var runtime: Int? = null,
    var episodeRunTime: Int? = null,
    var popularity: Double? = null,
    var voteCount: Int? = null,
    var posterPath: String? = null,
    var originalLanguage: String? = null,
    var voteAverage: Double? = null,
    var overview: String? = null,
    var type: Int? = null
): Parcelable
package com.dlgsoft.mcinema.api.models

import com.google.gson.annotations.SerializedName

data class MovieItemApiObj(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("poster_path")
    val posterUrl: String,
    @SerializedName("vote_average")
    val voteAvg: Double,
    @SerializedName("release_date")
    val releaseDate: String
) {
    /*fun toLocalDbObj() = MovieListItem(
        id = id,
        adult = adult,
        posterUrl = posterUrl,
        description = description,
        title = title,
        voteAvg = voteAvg,
        popularity = popularity,
        timestamp = System.nanoTime()
    )*/
}

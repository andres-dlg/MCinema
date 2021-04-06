package com.dlgsoft.mcinema.api.models

import com.dlgsoft.mcinema.data.db.models.Movie
import com.google.gson.annotations.SerializedName

data class MovieApiObj(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("poster_path")
    val posterUrl: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("backdrop_path")
    val backdropUrl: String,
    @SerializedName("vote_average")
    val voteAvg: Double,
    @SerializedName("vote_count")
    val votes: Int,
    @SerializedName("genres")
    val genres: List<MovieGenreApiObj>,
) {

    fun toLocalDbObj() = Movie(
        id = id,
        title = title,
        adult = adult,
        posterUrl = posterUrl,
        backdropUrl = backdropUrl,
        voteAvg = voteAvg,
        votes = votes,
        overview = overview
    )
}
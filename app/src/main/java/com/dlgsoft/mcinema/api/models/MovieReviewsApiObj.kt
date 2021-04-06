package com.dlgsoft.mcinema.api.models

import com.dlgsoft.mcinema.data.db.models.MovieReviews
import com.google.gson.annotations.SerializedName

data class MovieReviewsApiObj(
    @SerializedName("page")
    val page: Int,
    @SerializedName("total_results")
    val totalReviews: Int,
    @SerializedName("results")
    val reviews: List<MovieReviewApiObj>,
) {
    fun toLocalDbObj(movieId: Long) = MovieReviews(
        id = null,
        totalReviews = totalReviews,
        movieId = movieId
    )
}
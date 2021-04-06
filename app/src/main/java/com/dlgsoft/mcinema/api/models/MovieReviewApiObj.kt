package com.dlgsoft.mcinema.api.models

import com.dlgsoft.mcinema.data.db.models.Genre
import com.dlgsoft.mcinema.data.db.models.Review
import com.google.gson.annotations.SerializedName

data class MovieReviewApiObj(
    @SerializedName("id")
    val id: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("author_details")
    val authorDetails: MovieReviewAuthorApiObj,
    @SerializedName("content")
    val content: String
) {
    fun toLocalDbObj(movieReviewsId: Long) = Review(
        id = id,
        movieReviewId = movieReviewsId,
        author = author,
        avatarUrl = authorDetails.getAvatarPath(),
        rating = authorDetails.rating,
        content = content
    )
}
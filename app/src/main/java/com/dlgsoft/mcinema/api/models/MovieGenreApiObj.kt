package com.dlgsoft.mcinema.api.models

import com.dlgsoft.mcinema.data.db.models.Genre
import com.google.gson.annotations.SerializedName

data class MovieGenreApiObj(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
) {
    fun toLocalDbObj(movieId: Long) = Genre(
        id = id,
        genre = name,
        movieId = movieId
    )
}
package com.dlgsoft.mcinema.api.models

import com.google.gson.annotations.SerializedName

data class MoviesApiObj(
    @SerializedName("page")
    val page: Int,
    @SerializedName("result")
    val result: List<MovieItemApiObj>,
)
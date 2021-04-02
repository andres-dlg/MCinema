package com.dlgsoft.mcinema.api.models

import com.google.gson.annotations.SerializedName

data class MovieGenreApiObj(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
)
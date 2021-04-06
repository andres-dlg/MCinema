package com.dlgsoft.mcinema.api.models

import com.google.gson.annotations.SerializedName

data class MovieReviewAuthorApiObj(
    @SerializedName("name")
    val name: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("avatar_path")
    val avatarUrl: String,
    @SerializedName("rating")
    val rating: Double,
) {
    fun getAvatarPath(): String = if (avatarUrl.startsWith("/http")) {
        avatarUrl.removePrefix("/")
    } else {
        avatarUrl
    }
}
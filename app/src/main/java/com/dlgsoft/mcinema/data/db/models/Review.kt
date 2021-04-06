package com.dlgsoft.mcinema.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Review(
    @PrimaryKey
    val id: String,
    val movieReviewId: Long,
    val author: String,
    val avatarUrl: String,
    val rating: Double,
    val content: String,
)
package com.dlgsoft.mcinema.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieReviews(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val totalReviews: Int,
    val movieId: Long,
    val updatedAt: Long = System.nanoTime()
)
package com.dlgsoft.mcinema.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie(
    @PrimaryKey
    val id: Long,
    val title: String,
    val adult: Boolean,
    val posterUrl: String?,
    val backdropUrl: String?,
    val voteAvg: Double,
    val votes: Int,
    val overview: String?,
    val updatedAt: Long = System.nanoTime()
)
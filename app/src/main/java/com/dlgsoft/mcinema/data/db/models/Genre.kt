package com.dlgsoft.mcinema.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Genre(
    @PrimaryKey
    val id: Long,
    val genre: String,
    val movieId: Long,
    val updatedAt: Long = System.nanoTime()
)
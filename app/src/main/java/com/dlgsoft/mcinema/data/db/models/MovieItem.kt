package com.dlgsoft.mcinema.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_item")
data class MovieItem(
    @PrimaryKey
    val id: Long,
    val title: String,
    val adult: Boolean,
    @ColumnInfo(defaultValue = "") val posterUrl: String?,
    val voteAvg: Double,
    val releaseDate: String,
    val popularity: Double,
    val updatedAt: Long = System.nanoTime()
)
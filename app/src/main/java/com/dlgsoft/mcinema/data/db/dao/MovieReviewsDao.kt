package com.dlgsoft.mcinema.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dlgsoft.mcinema.data.db.models.MovieReviews

@Dao
interface MovieReviewsDao {
    @Query("DELETE FROM moviereviews")
    suspend fun removeMovieReviews()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movieReviews: MovieReviews): Long
}
package com.dlgsoft.mcinema.data.db.dao

import androidx.room.*
import com.dlgsoft.mcinema.data.db.models.MovieReviews
import com.dlgsoft.mcinema.data.db.relations.ReviewsWithTotal
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieReviewsDao {
    @Query("DELETE FROM moviereviews")
    suspend fun removeMovieReviews()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movieReviews: MovieReviews): Long

    @Query(
        """
        SELECT mr.id
        FROM moviereviews mr 
        WHERE mr.movieId = :movieId
    """
    )
    fun getIdByMovieId(movieId: Long): Long

    @Transaction
    @Query(
        """
        SELECT *
        FROM moviereviews mr 
        WHERE mr.movieId = :movieId
    """
    )
    fun getReviewsByMovieId(movieId: Long): Flow<ReviewsWithTotal>
}
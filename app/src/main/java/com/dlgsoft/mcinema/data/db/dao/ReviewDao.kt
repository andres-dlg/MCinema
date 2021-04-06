package com.dlgsoft.mcinema.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dlgsoft.mcinema.data.db.models.Review
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Query("DELETE FROM review")
    suspend fun removeReview()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reviews: List<Review>)

    @Query(
        """
        SELECT * FROM review r
        INNER JOIN moviereviews mr ON mr.id = r.movieReviewId
        WHERE mr.movieId = :movieId
    """
    )
    fun getReviewsByMovieId(movieId: Long): Flow<List<Review>>
}
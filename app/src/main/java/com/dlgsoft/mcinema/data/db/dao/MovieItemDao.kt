package com.dlgsoft.mcinema.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dlgsoft.mcinema.data.db.models.MovieItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieItemDao {
    @Query("SELECT * FROM movie_item ORDER BY updatedAt ASC")
    fun getMovieListItems(): Flow<List<MovieItem>>

    @Query("DELETE FROM movie_item")
    suspend fun removeMovieListItems()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movieListItems: List<MovieItem>)
}
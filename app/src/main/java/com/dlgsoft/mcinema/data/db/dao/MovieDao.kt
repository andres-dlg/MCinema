package com.dlgsoft.mcinema.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dlgsoft.mcinema.data.db.models.Movie
import com.dlgsoft.mcinema.data.db.relations.MovieWithGenres
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie WHERE id = :id")
    fun getMovieById(id: Long): Flow<MovieWithGenres>

    @Query("DELETE FROM movie")
    suspend fun removeMovie()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)
}
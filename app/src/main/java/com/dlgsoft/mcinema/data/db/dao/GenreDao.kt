package com.dlgsoft.mcinema.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dlgsoft.mcinema.data.db.models.Genre

@Dao
interface GenreDao {
    @Query("DELETE FROM genre")
    suspend fun removeGenre()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(genres: List<Genre>)
}
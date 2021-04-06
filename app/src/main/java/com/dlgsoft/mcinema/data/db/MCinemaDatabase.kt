package com.dlgsoft.mcinema.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dlgsoft.mcinema.data.db.dao.*
import com.dlgsoft.mcinema.data.db.models.*

@Database(
    entities = [MovieItem::class, Movie::class, Genre::class, MovieReviews::class, Review::class],
    version = 1,
)
abstract class MCinemaDatabase : RoomDatabase() {

    abstract fun movieItemDao(): MovieItemDao
    abstract fun movieDao(): MovieDao
    abstract fun genreDao(): GenreDao
    abstract fun movieReviewsDao(): MovieReviewsDao
    abstract fun reviewDao(): ReviewDao

    companion object {
        @Volatile
        private var instance: MCinemaDatabase? = null

        fun getDatabase(context: Context): MCinemaDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, MCinemaDatabase::class.java, "mcinema_db")
                .fallbackToDestructiveMigration()
                .build()
    }
}

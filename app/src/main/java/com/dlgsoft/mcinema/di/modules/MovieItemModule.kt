package com.dlgsoft.mcinema.di.modules

import android.content.SharedPreferences
import com.dlgsoft.mcinema.api.MCinemaApi
import com.dlgsoft.mcinema.data.db.MCinemaDatabase
import com.dlgsoft.mcinema.data.db.dao.MovieItemDao
import com.dlgsoft.mcinema.data.repositories.MoviesRepository
import com.dlgsoft.mcinema.data.repositories.MoviesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MovieItemModule {

    @Singleton
    @Provides
    fun provideMovieItemDao(db: MCinemaDatabase): MovieItemDao =
        db.movieItemDao()

    @Singleton
    @Provides
    fun provideMoviesRepository(
        api: MCinemaApi,
        db: MCinemaDatabase,
        movieItemDao: MovieItemDao,
        sharedPreferences: SharedPreferences
    ): MoviesRepository =
        MoviesRepositoryImpl(api, db, movieItemDao, sharedPreferences)
}
package com.dlgsoft.mcinema.di.modules

import com.dlgsoft.mcinema.api.MCinemaApi
import com.dlgsoft.mcinema.data.db.MCinemaDatabase
import com.dlgsoft.mcinema.data.db.dao.GenreDao
import com.dlgsoft.mcinema.data.db.dao.MovieDao
import com.dlgsoft.mcinema.data.repositories.MovieRepository
import com.dlgsoft.mcinema.data.repositories.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MovieModule {
    @Singleton
    @Provides
    fun provideMovieDao(db: MCinemaDatabase): MovieDao =
        db.movieDao()

    @Singleton
    @Provides
    fun provideGenreDao(db: MCinemaDatabase): GenreDao =
        db.genreDao()

    @Singleton
    @Provides
    fun provideMovieRepository(
        api: MCinemaApi,
        db: MCinemaDatabase,
        movieDao: MovieDao,
        genreDao: GenreDao
    ): MovieRepository =
        MovieRepositoryImpl(api, db, movieDao, genreDao)
}

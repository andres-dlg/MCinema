package com.dlgsoft.mcinema.di.modules

import com.dlgsoft.mcinema.api.MCinemaApi
import com.dlgsoft.mcinema.data.db.MCinemaDatabase
import com.dlgsoft.mcinema.data.db.dao.GenreDao
import com.dlgsoft.mcinema.data.db.dao.MovieDao
import com.dlgsoft.mcinema.data.db.dao.MovieReviewsDao
import com.dlgsoft.mcinema.data.db.dao.ReviewDao
import com.dlgsoft.mcinema.data.repositories.MovieRepository
import com.dlgsoft.mcinema.data.repositories.MovieRepositoryImpl
import com.dlgsoft.mcinema.data.repositories.MovieReviewsRepository
import com.dlgsoft.mcinema.data.repositories.MovieReviewsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ReviewsModule {
    @Singleton
    @Provides
    fun provideMovieReviewsDao(db: MCinemaDatabase): MovieReviewsDao =
        db.movieReviewsDao()

    @Singleton
    @Provides
    fun provideReviewDao(db: MCinemaDatabase): ReviewDao =
        db.reviewDao()

    @Singleton
    @Provides
    fun provideMovieReviewsRepository(
        api: MCinemaApi,
        db: MCinemaDatabase,
        movieReviewsDao: MovieReviewsDao,
        reviewDao: ReviewDao
    ): MovieReviewsRepository =
        MovieReviewsRepositoryImpl(api, db, movieReviewsDao, reviewDao)
}

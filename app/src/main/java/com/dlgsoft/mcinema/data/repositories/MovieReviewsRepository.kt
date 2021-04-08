package com.dlgsoft.mcinema.data.repositories

import androidx.room.withTransaction
import com.dlgsoft.mcinema.api.MCinemaApi
import com.dlgsoft.mcinema.data.db.MCinemaDatabase
import com.dlgsoft.mcinema.data.db.dao.MovieReviewsDao
import com.dlgsoft.mcinema.data.db.dao.ReviewDao
import com.dlgsoft.mcinema.data.db.relations.ReviewsWithTotal
import com.dlgsoft.mcinema.utils.Resource
import com.dlgsoft.mcinema.utils.networkBoundResource
import kotlinx.coroutines.flow.Flow

interface MovieReviewsRepository {
    suspend fun getReviews(
        id: Long,
        page: Int?
    ): Flow<Resource<ReviewsWithTotal>>
}

class MovieReviewsRepositoryImpl(
    private val api: MCinemaApi,
    private val db: MCinemaDatabase,
    private val movieReviewsDao: MovieReviewsDao,
    private val reviewDao: ReviewDao,
) : MovieReviewsRepository {

    override suspend fun getReviews(
        id: Long,
        page: Int?
    ): Flow<Resource<ReviewsWithTotal>> = networkBoundResource(
        query = { movieReviewsDao.getReviewsByMovieId(id) },
        fetch = { api.getMovieReviews(id, page) },
        saveFetchResult = { m ->
            db.withTransaction {
                val movieReviews = m.toLocalDbObj(id)
                val movieReviewsId = if (page == 1) {
                    movieReviewsDao.removeMovieReviews()
                    reviewDao.removeReview()
                    movieReviewsDao.insert(movieReviews)
                } else {
                    movieReviewsDao.getIdByMovieId(id)
                }
                val reviews = m.reviews.map { it.toLocalDbObj(movieReviewsId) }
                reviewDao.insertAll(reviews)
            }
        },
        shouldFetch = { true }
    )
}
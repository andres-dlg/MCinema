package com.dlgsoft.mcinema.data.repositories

import androidx.room.withTransaction
import com.dlgsoft.mcinema.api.MCinemaApi
import com.dlgsoft.mcinema.data.db.MCinemaDatabase
import com.dlgsoft.mcinema.data.db.dao.GenreDao
import com.dlgsoft.mcinema.data.db.dao.MovieDao
import com.dlgsoft.mcinema.data.db.dao.MovieReviewsDao
import com.dlgsoft.mcinema.data.db.dao.ReviewDao
import com.dlgsoft.mcinema.data.db.models.Review
import com.dlgsoft.mcinema.data.db.relations.MovieWithGenres
import com.dlgsoft.mcinema.utils.Resource
import com.dlgsoft.mcinema.utils.networkBoundResource
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

interface MovieReviewsRepository {
    suspend fun getReviews(
        id: Long,
        page: Int?,
        onFetchSuccess: () -> Unit,
        onFetchFailed: (Throwable) -> Unit
    ): Flow<Resource<List<Review>>>
}

class MovieReviewsRepositoryImpl(
    private val api: MCinemaApi,
    private val db: MCinemaDatabase,
    private val movieReviewsDao: MovieReviewsDao,
    private val reviewDao: ReviewDao,
) : MovieReviewsRepository {

    override suspend fun getReviews(
        id: Long,
        page: Int?,
        onFetchSuccess: () -> Unit,
        onFetchFailed: (Throwable) -> Unit
    ): Flow<Resource<List<Review>>> = networkBoundResource(
        query = { reviewDao.getReviewsByMovieId(id) },
        fetch = { api.getMovieReviews(id, page) },
        saveFetchResult = { m ->
            db.withTransaction {
                val movieReviews = m.toLocalDbObj(id)
                val movieReviewsId = movieReviewsDao.insert(movieReviews)
                val reviews = m.reviews.map { it.toLocalDbObj(movieReviewsId) }
                reviewDao.insertAll(reviews)
            }
        },
        shouldFetch = { true },
        onFetchSuccess = onFetchSuccess,
        onFetchFailed = { t ->
            if (t !is HttpException && t !is IOException) {
                throw t
            }
            onFetchFailed(t)
        }
    )
}
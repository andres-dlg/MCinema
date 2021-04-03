package com.dlgsoft.mcinema.data.repositories

import androidx.room.withTransaction
import com.dlgsoft.mcinema.api.MCinemaApi
import com.dlgsoft.mcinema.data.db.MCinemaDatabase
import com.dlgsoft.mcinema.data.db.dao.MovieItemDao
import com.dlgsoft.mcinema.data.db.models.MovieItem
import com.dlgsoft.mcinema.utils.Resource
import com.dlgsoft.mcinema.utils.networkBoundResource
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

interface MoviesRepository {
    suspend fun getMovieListItems(
        sort: String?,
        page: Int?,
        query: String?,
        forceRefresh: Boolean,
        onFetchSuccess: () -> Unit,
        onFetchFailed: (Throwable) -> Unit
    ): Flow<Resource<List<MovieItem>>>
}

class MoviesRepositoryImpl(
    private val api: MCinemaApi,
    private val db: MCinemaDatabase,
    private val movieItemDao: MovieItemDao,
) : MoviesRepository {

    /**
     * Gets movies list from network, saves them into the local DB and returns the list
     **/
    override suspend fun getMovieListItems(
        sort: String?,
        page: Int?,
        query: String?,
        forceRefresh: Boolean,
        onFetchSuccess: () -> Unit,
        onFetchFailed: (Throwable) -> Unit
    ): Flow<Resource<List<MovieItem>>> = networkBoundResource(
        query = { movieItemDao.getMovieListItems() },
        fetch = { api.getMovies(sort, page, query) },
        saveFetchResult = { response ->
            val moviesList = response.movies.map { it.toLocalDbObj() }
            db.withTransaction {
                if (page == 1) {
                    movieItemDao.removeMovieListItems()
                }
                movieItemDao.insertAll(moviesList)
            }
        },
        shouldFetch = { movieItems ->
            if (forceRefresh) {
                true
            } else {
                val sortedMovieItems = movieItems.sortedBy { movieItem ->
                    movieItem.updatedAt
                }
                val oldestTimestamp = sortedMovieItems.firstOrNull()?.updatedAt
                val needsRefresh = oldestTimestamp == null ||
                        oldestTimestamp < System.nanoTime() -
                        TimeUnit.MINUTES.toMillis(60)
                needsRefresh
            }
        },
        onFetchSuccess = onFetchSuccess,
        onFetchFailed = { t ->
            if (t !is HttpException && t !is IOException) {
                throw t
            }
            onFetchFailed(t)
        }
    )
}

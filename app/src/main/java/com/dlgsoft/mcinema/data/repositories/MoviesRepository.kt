package com.dlgsoft.mcinema.data.repositories

import android.content.SharedPreferences
import androidx.room.withTransaction
import com.dlgsoft.mcinema.api.MCinemaApi
import com.dlgsoft.mcinema.data.db.MCinemaDatabase
import com.dlgsoft.mcinema.data.db.dao.MovieItemDao
import com.dlgsoft.mcinema.data.db.models.MovieItem
import com.dlgsoft.mcinema.utils.Constants.PREFS_EXPIRATION_DATE_KEY
import com.dlgsoft.mcinema.utils.Resource
import com.dlgsoft.mcinema.utils.networkBoundResource
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

interface MoviesRepository {
    suspend fun getMovieListItems(
        page: Int?,
        forceRefresh: Boolean
    ): Flow<Resource<List<MovieItem>>>
}

class MoviesRepositoryImpl(
    private val api: MCinemaApi,
    private val db: MCinemaDatabase,
    private val movieItemDao: MovieItemDao,
    private val sharedPreferences: SharedPreferences
) : MoviesRepository {

    /**
     * Gets movies list from network, saves them into the local DB and returns the list
     **/
    override suspend fun getMovieListItems(
        page: Int?,
        forceRefresh: Boolean
    ): Flow<Resource<List<MovieItem>>> = networkBoundResource(
        query = {
            val expirationDate = sharedPreferences.getLong(PREFS_EXPIRATION_DATE_KEY, 0)
            movieItemDao.getMovieListItems(
                System.currentTimeMillis(),
                if (expirationDate == 0L) 86400000 else expirationDate
            )
        },
        fetch = { api.getMovies(page) },
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
        }
    )
}

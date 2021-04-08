package com.dlgsoft.mcinema.data.repositories

import androidx.room.withTransaction
import com.dlgsoft.mcinema.api.MCinemaApi
import com.dlgsoft.mcinema.data.db.MCinemaDatabase
import com.dlgsoft.mcinema.data.db.dao.GenreDao
import com.dlgsoft.mcinema.data.db.dao.MovieDao
import com.dlgsoft.mcinema.data.db.relations.MovieWithGenres
import com.dlgsoft.mcinema.utils.Resource
import com.dlgsoft.mcinema.utils.networkBoundResource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovie(
        id: Long
    ): Flow<Resource<MovieWithGenres>>
}

class MovieRepositoryImpl(
    private val api: MCinemaApi,
    private val db: MCinemaDatabase,
    private val movieDao: MovieDao,
    private val genreDao: GenreDao,
) : MovieRepository {

    override suspend fun getMovie(
        id: Long
    ): Flow<Resource<MovieWithGenres>> = networkBoundResource(
        query = { movieDao.getMovieById(id) },
        fetch = { api.getMovie(id) },
        saveFetchResult = { m ->
            val movie = m.toLocalDbObj()
            val genres = m.genres.map { it.toLocalDbObj(movie.id) }
            db.withTransaction {
                movieDao.removeMovie()
                genreDao.removeGenre()
                movieDao.insert(movie)
                genreDao.insertAll(genres)
            }
        },
        shouldFetch = { true }
    )
}
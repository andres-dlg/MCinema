package com.dlgsoft.mcinema.api

import com.dlgsoft.mcinema.api.models.MovieApiObj
import com.dlgsoft.mcinema.api.models.MoviesApiObj
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MCinemaApi {

    @GET("discover/movie")
    suspend fun getMovies(
        @Query("sort") sort: String?,
        @Query("page") page: Int?,
        @Query("q") query: String?,
    ): Response<MoviesApiObj>

    @GET("movie/{id}")
    suspend fun getMovie(
        @Path("id") id: Long
    ): Response<MovieApiObj>

}
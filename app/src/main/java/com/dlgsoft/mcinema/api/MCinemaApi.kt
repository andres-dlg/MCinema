package com.dlgsoft.mcinema.api

import com.dlgsoft.mcinema.BuildConfig
import com.dlgsoft.mcinema.api.models.MovieApiObj
import com.dlgsoft.mcinema.api.models.MovieReviewsApiObj
import com.dlgsoft.mcinema.api.models.MoviesApiObj
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MCinemaApi {

    @Headers("Authorization: ${BuildConfig.API_KEY}")
    @GET("discover/movie")
    suspend fun getMovies(
        @Query("page") page: Int?,
    ): MoviesApiObj

    @Headers("Authorization: ${BuildConfig.API_KEY}")
    @GET("movie/{id}")
    suspend fun getMovie(
        @Path("id") id: Long
    ): MovieApiObj

    @Headers("Authorization: ${BuildConfig.API_KEY}")
    @GET("movie/{id}/reviews")
    suspend fun getMovieReviews(
        @Path("id") id: Long,
        @Query("page") page: Int?,
    ): MovieReviewsApiObj

}
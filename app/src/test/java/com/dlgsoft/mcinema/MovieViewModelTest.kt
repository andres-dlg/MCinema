package com.dlgsoft.mcinema

import com.dlgsoft.mcinema.data.db.models.Movie
import com.dlgsoft.mcinema.data.db.models.MovieItem
import com.dlgsoft.mcinema.data.db.models.Review
import com.dlgsoft.mcinema.data.db.relations.MovieWithGenres
import com.dlgsoft.mcinema.data.db.relations.ReviewsWithTotal
import com.dlgsoft.mcinema.data.repositories.MovieRepository
import com.dlgsoft.mcinema.data.repositories.MovieReviewsRepository
import com.dlgsoft.mcinema.ui.features.movie.MovieViewModel
import com.dlgsoft.mcinema.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.io.IOException

@RunWith(JUnit4::class)
class MovieViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    lateinit var movieViewModel: MovieViewModel

    @Mock
    lateinit var movieRepository: MovieRepository

    @Mock
    lateinit var reviewsRepository: MovieReviewsRepository

    @Mock
    lateinit var movie: MovieWithGenres

    @Mock
    lateinit var reviews: ReviewsWithTotal

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.openMocks(this)
        movieViewModel = MovieViewModel(movieRepository, reviewsRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun test_getMovie_success() = runBlocking {
        // Prepare
        val successResponse = Resource.Success(movie)
        `when`(movieRepository.getMovie(anyLong())).thenReturn(
            flowOf(successResponse)
        )

        // Action
        val result = movieRepository.getMovie(1).first()

        // Assert
        assertThat(result, instanceOf(Resource.Success::class.java))
        assertThat(result.data, `is`(movie))
    }

    @Test
    fun test_getMovie_failure() = runBlocking {
        // Prepare
        val failureResponse = Resource.Error<MovieWithGenres>(IOException(), null)
        `when`(movieRepository.getMovie(anyLong())).thenReturn(
            flowOf(failureResponse)
        )

        // Action
        val result = movieRepository.getMovie(1).first()

        // Assert
        assertThat(result, instanceOf(Resource.Error::class.java))
        assert(result.data == null)
    }

    @Test
    fun test_getMovieReviews_success() = runBlocking {
        // Prepare
        val successResponse = Resource.Success(reviews)
        `when`(reviewsRepository.getReviews(anyLong(), anyInt())).thenReturn(
            flowOf(successResponse)
        )

        // Action
        val result = reviewsRepository.getReviews(1, 1).first()

        // Assert
        assertThat(result, instanceOf(Resource.Success::class.java))
        assertThat(result.data, `is`(reviews))
    }

    @Test
    fun test_getMovieReviews_failure() = runBlocking {
        // Prepare
        val failureResponse = Resource.Error(IOException(), reviews)
        `when`(reviewsRepository.getReviews(anyLong(), anyInt())).thenReturn(
            flowOf(failureResponse)
        )

        // Action
        val result = reviewsRepository.getReviews(1, 1).first()

        // Assert
        assertThat(result, instanceOf(Resource.Error::class.java))
        assert(result.data != null)
    }
}
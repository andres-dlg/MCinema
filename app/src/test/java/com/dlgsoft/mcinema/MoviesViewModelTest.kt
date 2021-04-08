package com.dlgsoft.mcinema

import android.content.SharedPreferences
import com.dlgsoft.mcinema.data.db.models.MovieItem
import com.dlgsoft.mcinema.data.repositories.MoviesRepository
import com.dlgsoft.mcinema.ui.features.movies.MoviesViewModel
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
class MoviesViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    lateinit var moviesViewModel: MoviesViewModel

    @Mock
    lateinit var repository: MoviesRepository

    @Mock
    lateinit var sharedPreferences: SharedPreferences

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.openMocks(this)
        moviesViewModel = MoviesViewModel(repository, sharedPreferences)
        moviesViewModel.onStart()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun test_getMovies_success() = runBlocking {
        // Prepare
        val successResponse = Resource.Success(listOf<MovieItem>())
        `when`(repository.getMovieListItems(anyInt(), anyBoolean())).thenReturn(
            flowOf(successResponse)
        )

        // Action
        val result = repository.getMovieListItems(1, true).first()

        // Assert
        assertThat(result, instanceOf(Resource.Success::class.java))
    }

    @Test
    fun test_getMovies_withoutCache_failure() = runBlocking {
        // Prepare
        val failureResponse = Resource.Error<List<MovieItem>>(IOException(), null)
        `when`(repository.getMovieListItems(anyInt(), anyBoolean())).thenReturn(
            flowOf(failureResponse)
        )

        // Action
        val result = repository.getMovieListItems(1, true).first()

        // Assert
        assertThat(result, instanceOf(Resource.Error::class.java))
        assert(result.data.isNullOrEmpty())
    }

    @Test
    fun test_getMovies_withCache_failure() = runBlocking {
        // Prepare
        val failureResponse = Resource.Error<List<MovieItem>>(IOException(), listOf())
        `when`(repository.getMovieListItems(anyInt(), anyBoolean())).thenReturn(
            flowOf(failureResponse)
        )

        // Action
        val result = repository.getMovieListItems(1, true).first()

        // Assert
        assertThat(result, instanceOf(Resource.Error::class.java))
        assert(result.data != null)
    }
}
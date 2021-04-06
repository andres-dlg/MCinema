package com.dlgsoft.mcinema.ui.features.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlgsoft.mcinema.data.repositories.MovieRepository
import com.dlgsoft.mcinema.data.repositories.MovieReviewsRepository
import com.dlgsoft.mcinema.ui.features.movies.MoviesViewModel
import com.dlgsoft.mcinema.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val reviewsRepository: MovieReviewsRepository,
) :
    ViewModel() {

    private var page = 1

    private val movieTriggerChannel = Channel<Long>()
    private val movieTrigger = movieTriggerChannel.receiveAsFlow()

    private val reviewsTriggerChannel = Channel<Long>()
    private val reviewsTrigger = reviewsTriggerChannel.receiveAsFlow()

    val movie = movieTrigger.flatMapLatest { id ->
        movieRepository.getMovie(id, {}, {})
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val reviews = reviewsTrigger.flatMapLatest { id ->
        reviewsRepository.getReviews(id, page, {}, {})
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun getMovie(id: Long) {
        if (movie.value !is Resource.Loading) {
            viewModelScope.launch {
                movieTriggerChannel.send(id)
            }
        }
    }

    fun getReviews(id: Long) {
        if (reviews.value !is Resource.Loading) {
            viewModelScope.launch {
                reviewsTriggerChannel.send(id)
            }
        }
    }

    fun loadNextPage(id: Long) {
        page += 1
        getReviews(id)
    }
}
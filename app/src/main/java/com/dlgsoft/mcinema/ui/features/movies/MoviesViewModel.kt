package com.dlgsoft.mcinema.ui.features.movies

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlgsoft.mcinema.data.repositories.MoviesRepository
import com.dlgsoft.mcinema.ui.dialogs.ExpirationDate
import com.dlgsoft.mcinema.utils.Constants.PREFS_EXPIRATION_DATE_KEY
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
class MoviesViewModel @Inject constructor(
    moviesRepository: MoviesRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private var page: Int = 1

    private val refreshTriggerChannel = Channel<Refresh>()
    private val refreshTrigger = refreshTriggerChannel.receiveAsFlow()

    val movies = refreshTrigger.flatMapLatest { refresh ->
        moviesRepository.getMovieListItems(
            page,
            forceRefresh = refresh == Refresh.FORCE,
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun onStart() {
        if (movies.value !is Resource.Loading) {
            viewModelScope.launch {
                refreshTriggerChannel.send(Refresh.NORMAL)
            }
        }
    }

    fun onManualRefresh() {
        if (movies.value !is Resource.Loading) {
            viewModelScope.launch {
                refreshTriggerChannel.send(Refresh.FORCE)
            }
        }
    }

    fun loadNextPage() {
        page += 1
        onStart()
    }

    fun getExpirationDate(): ExpirationDate {
        val expirationDateInMillis = sharedPreferences.getLong(PREFS_EXPIRATION_DATE_KEY, 0)
        return ExpirationDate(expirationDateInMillis)
    }

    fun saveExpirationDate(it: Long) {
        sharedPreferences.edit().putLong(PREFS_EXPIRATION_DATE_KEY, it).apply()
    }

    enum class Refresh {
        FORCE, NORMAL
    }
}
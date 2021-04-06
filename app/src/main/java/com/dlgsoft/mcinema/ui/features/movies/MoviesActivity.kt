package com.dlgsoft.mcinema.ui.features.movies

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView
import com.dlgsoft.mcinema.R
import com.dlgsoft.mcinema.databinding.ActivityMoviesBinding
import com.dlgsoft.mcinema.extensions.exhaustive
import com.dlgsoft.mcinema.ui.features.movie.MovieActivity
import com.dlgsoft.mcinema.ui.features.movie.MovieActivity.Companion.EXTRA_MOVIE_ID
import com.dlgsoft.mcinema.utils.EndlessRecyclerOnScrollListener
import com.dlgsoft.mcinema.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MoviesActivity : AppCompatActivity() {

    private val viewModel: MoviesViewModel by viewModels()

    private val moviesController by lazy {
        MoviesController {
            startActivity(Intent(this, MovieActivity::class.java).apply {
                putExtra(EXTRA_MOVIE_ID, it)
            })
        }
    }

    private lateinit var scrollListener: EndlessRecyclerOnScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMoviesBinding.inflate(layoutInflater)
        val list = binding.list
        val progress = binding.progress
        setContentView(binding.root)

        setupList(list)

        /*viewModel.movies
            .onEach {
                val result = it ?: return@onEach
                scrollListener.apply {
                    listSize = result.data?.size ?: 0
                    isLoading = false
                }
                moviesController.setData(result.data)
                progress.isVisible = result is Resource.Loading
            }
            .launchIn(lifecycleScope)*/


        lifecycleScope.launchWhenStarted {
            viewModel.movies.collect {
                val result = it ?: return@collect
                scrollListener.apply {
                    listSize = result.data?.size ?: 0
                    isLoading = false
                }
                moviesController.setData(result.data)
                progress.isVisible = result is Resource.Loading
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.events.collect { event ->
                when (event) {
                    is MoviesViewModel.Event.ShowErrorMessage ->
                        Snackbar.make(
                            binding.root,
                            getString(
                                R.string.could_not_refresh,
                                event.error.localizedMessage
                                    ?: getString(R.string.unknown_error_occurred)
                            ),
                            Snackbar.LENGTH_LONG
                        ).setAction(R.string.retry) {
                            viewModel.onManualRefresh()
                        }.show()
                }.exhaustive
            }
        }
    }

    private fun setupList(list: EpoxyRecyclerView) {
        val spanCount = 2
        val gridLayoutManager = GridLayoutManager(this, spanCount)
        gridLayoutManager.spanSizeLookup = moviesController.adapter.spanSizeLookup
        list.layoutManager = gridLayoutManager
        list.adapter = moviesController.adapter
        scrollListener = EndlessRecyclerOnScrollListener(
            layoutManager = list.layoutManager as GridLayoutManager,
            listSize = list.size,
            callback = {
                viewModel.loadNextPage()
            }
        )
        list.addOnScrollListener(scrollListener)
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }
}
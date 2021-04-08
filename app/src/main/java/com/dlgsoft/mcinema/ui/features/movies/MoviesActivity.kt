package com.dlgsoft.mcinema.ui.features.movies

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView
import com.dlgsoft.mcinema.R
import com.dlgsoft.mcinema.databinding.ActivityMoviesBinding
import com.dlgsoft.mcinema.ui.dialogs.ExpirationDateDialog
import com.dlgsoft.mcinema.ui.features.movie.MovieActivity
import com.dlgsoft.mcinema.ui.features.movie.MovieActivity.Companion.EXTRA_MOVIE_ID
import com.dlgsoft.mcinema.utils.EndlessRecyclerOnScrollListener
import com.dlgsoft.mcinema.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


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
        val toolbar = binding.toolbar
        val noMoviesImage = binding.noMoviesImage
        val noMoviesText = binding.noMoviesText
        setContentView(binding.root)

        setSupportActionBar(toolbar)
        setupList(list)

        lifecycleScope.launchWhenStarted {
            viewModel.movies.collect { event ->
                event ?: return@collect
                progress.isVisible = event is Resource.Loading
                when (event) {
                    is Resource.Error -> {
                        moviesController.setData(event.data)
                        val noMovies =
                            moviesController.currentData == null || moviesController.currentData?.size == 0
                        noMoviesImage.isVisible = noMovies
                        noMoviesText.isVisible = noMovies
                        Snackbar.make(
                            binding.root,
                            getString(
                                R.string.could_not_refresh,
                                event.error?.localizedMessage
                                    ?: getString(R.string.unknown_error_occurred)
                            ),
                            Snackbar.LENGTH_LONG
                        ).setAction(R.string.retry) {
                            viewModel.onManualRefresh()
                        }.show()
                    }
                    is Resource.Success -> {
                        scrollListener.apply {
                            listSize = event.data?.size ?: 0
                            isLoading = false
                        }
                        moviesController.setData(event.data)
                    }
                    else -> {
                        // Nothing to do
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        scrollListener.listSize = 0
    }

    private fun setupList(list: EpoxyRecyclerView) {
        val spanCount = 2
        val gridLayoutManager = GridLayoutManager(this, spanCount)
        gridLayoutManager.spanSizeLookup = moviesController.adapter.spanSizeLookup
        list.layoutManager = gridLayoutManager
        list.adapter = moviesController.adapter
        scrollListener = EndlessRecyclerOnScrollListener(
            layoutManager = list.layoutManager as LinearLayoutManager,
            listSize = list.size,
            itemsUntilInvokeCallback = 11,
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.movies_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.date) {
            showFiltersDialog()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showFiltersDialog() {
        ExpirationDateDialog(this).apply {
            init(viewModel.getExpirationDate())
            setApplyButtonListener {
                viewModel.saveExpirationDate(it)
                dismiss()
            }
            show()
        }
    }
}
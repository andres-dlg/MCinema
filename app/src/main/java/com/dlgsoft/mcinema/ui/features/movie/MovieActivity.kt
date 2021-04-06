package com.dlgsoft.mcinema.ui.features.movie

import android.os.Bundle
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView
import com.dlgsoft.mcinema.R
import com.dlgsoft.mcinema.databinding.ActivityMovieBinding
import com.dlgsoft.mcinema.utils.EndlessRecyclerOnScrollListener
import com.dlgsoft.mcinema.utils.ImageLoader
import com.dlgsoft.mcinema.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MovieActivity : AppCompatActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader

    private val movieViewModel: MovieViewModel by viewModels()

    private val movieController by lazy {
        MovieController {
            movieViewModel.getReviews(it)
        }
    }

    private lateinit var root: ConstraintLayout
    private lateinit var toolbar: Toolbar
    private lateinit var list: EpoxyRecyclerView
    private lateinit var progress: ProgressBar

    private lateinit var scrollListener: EndlessRecyclerOnScrollListener

    private val movieId by lazy { intent.extras?.getLong(EXTRA_MOVIE_ID, 0) ?: 0 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMovieBinding.inflate(layoutInflater)
        toolbar = binding.toolbar
        progress = binding.progress
        list = binding.list
        root = binding.root
        setContentView(root)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.back)
            setDisplayHomeAsUpEnabled(true)
        }

        if (movieId > 0) {
            setupObserver()
            setupList()
            movieViewModel.getMovie(movieId)
        } else {
            finish()
        }
    }

    private fun setupList() {
        scrollListener = EndlessRecyclerOnScrollListener(
            layoutManager = list.layoutManager as LinearLayoutManager,
            listSize = list.size,
            itemsUntilInvokeCallback = 2,
            callback = {
                movieViewModel.loadNextPage(movieId)
            }
        )
        list.addOnScrollListener(scrollListener)
        list.setController(movieController)
    }

    private fun setupObserver() {
        lifecycleScope.launchWhenStarted {
            movieViewModel.movie.collect { event ->
                when (event) {
                    is Resource.Error -> {
                        progress.isVisible = true
                        Snackbar.make(
                            root,
                            getString(
                                R.string.could_not_refresh,
                                event.error?.localizedMessage
                                    ?: getString(R.string.unknown_error_occurred)
                            ),
                            Snackbar.LENGTH_LONG
                        ).setAction(R.string.retry) {
                            movieViewModel.getMovie(movieId)
                        }.show()
                    }
                    is Resource.Loading -> progress.isVisible = true
                    is Resource.Success -> {
                        progress.isVisible = false
                        movieController.movie = event.data
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            movieViewModel.reviews.collect { event ->
                when (event) {
                    is Resource.Error -> {
                        progress.isVisible = true
                        Snackbar.make(
                            root,
                            getString(
                                R.string.could_not_refresh,
                                event.error?.localizedMessage
                                    ?: getString(R.string.unknown_error_occurred)
                            ),
                            Snackbar.LENGTH_LONG
                        ).setAction(R.string.retry) {
                            movieViewModel.getReviews(movieId)
                        }.show()
                    }
                    is Resource.Loading -> progress.isVisible = true
                    is Resource.Success -> {
                        scrollListener.apply {
                            listSize = event.data?.reviews?.size ?: 0
                            isLoading = false
                        }
                        progress.isVisible = false
                        movieController.rwt = event.data
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_MOVIE_ID = "movie_id"
    }
}
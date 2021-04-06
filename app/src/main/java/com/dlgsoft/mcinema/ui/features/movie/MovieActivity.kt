package com.dlgsoft.mcinema.ui.features.movie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.dlgsoft.mcinema.R
import com.dlgsoft.mcinema.data.db.models.Movie
import com.dlgsoft.mcinema.data.db.relations.MovieWithGenres
import com.dlgsoft.mcinema.databinding.ActivityMovieBinding
import com.dlgsoft.mcinema.ui.views.AvgTextView
import com.dlgsoft.mcinema.ui.views.StarsView
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

    lateinit var root: ConstraintLayout
    lateinit var toolbar: Toolbar
    lateinit var title: TextView
    lateinit var votes: TextView
    lateinit var genres: TextView
    lateinit var votesAvg: AvgTextView
    lateinit var backdrop: ImageView
    lateinit var poster: ImageView
    lateinit var progress: ProgressBar
    lateinit var stars: StarsView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMovieBinding.inflate(layoutInflater)
        title = binding.title
        votes = binding.votes
        votesAvg = binding.avgScore
        genres = binding.genres
        stars = binding.stars
        backdrop = binding.backdrop
        poster = binding.poster
        toolbar = binding.toolbar
        progress = binding.progress
        root = binding.root
        setContentView(root)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.back)
            setDisplayHomeAsUpEnabled(true)
        }

        val id = intent.extras?.getLong(EXTRA_MOVIE_ID, 0) ?: 0
        if (id > 0) {
            setupObserver(id)
            movieViewModel.getMovie(id)
        } else {
            finish()
        }
    }

    private fun setupObserver(id: Long) {
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
                            movieViewModel.getMovie(id)
                        }.show()
                    }
                    is Resource.Loading -> progress.isVisible = true
                    is Resource.Success -> {
                        progress.isVisible = false
                        setupViews(event.data)
                    }
                }
            }
        }
    }

    private fun setupViews(data: MovieWithGenres?) {
        data?.let { m ->
            title.text = m.movie.title
            imageLoader.loadImage(poster, m.movie.posterUrl, "w342")
            imageLoader.loadImage(backdrop, m.movie.backdropUrl, "w300")
            votes.text = m.movie.votes.toString()
            votesAvg.apply {
                setScore(m.movie.voteAvg.toString())
                setTextColor(R.color.ruby)
                setAvgBackground(null)
                stars.setScore(m.movie.voteAvg)
            }
            genres.text = m.genres?.joinToString(", ") { it.genre }
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
package com.dlgsoft.mcinema.ui.features.movie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.dlgsoft.mcinema.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieActivity : AppCompatActivity() {

    private val movieController by lazy { MovieController() }

    private val movieViewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
    }

    companion object {
        const val EXTRA_MOVIE_ID = "movie_id"
    }
}
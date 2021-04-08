package com.dlgsoft.mcinema.ui.features.movies

import android.view.View
import com.airbnb.epoxy.TypedEpoxyController
import com.dlgsoft.mcinema.data.db.models.MovieItem
import com.dlgsoft.mcinema.ui.views.movieItemView
import java.text.SimpleDateFormat
import java.util.*

class MoviesController(private val onMovieClicked: (Long) -> Unit) :
    TypedEpoxyController<List<MovieItem>>() {

    private val sdf by lazy { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    override fun buildModels(data: List<MovieItem>?) {
        data?.forEach {
            var year = ""
            if (it.releaseDate.isNotBlank()) {
                val calendar = Calendar.getInstance()
                calendar.time = sdf.parse(it.releaseDate)!!
                year = calendar.get(Calendar.YEAR).toString()
            }
            movieItemView {
                id(it.id)
                title(it.title)
                imageUrl(it.posterUrl)
                isForAdults(it.adult)
                year(year)
                avg(it.voteAvg.toString())
                listener {
                    onMovieClicked.invoke(it.id)
                }
            }
        }
    }
}
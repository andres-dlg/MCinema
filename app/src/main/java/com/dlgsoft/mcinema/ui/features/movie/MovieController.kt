package com.dlgsoft.mcinema.ui.features.movie

import com.airbnb.epoxy.TypedEpoxyController
import com.dlgsoft.mcinema.data.db.models.Movie

class MovieController : TypedEpoxyController<Movie>() {
    override fun buildModels(data: Movie?) {
        data?.let {

        }
    }
}
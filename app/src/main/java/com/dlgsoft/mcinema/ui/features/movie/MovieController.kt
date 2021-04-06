package com.dlgsoft.mcinema.ui.features.movie

import com.airbnb.epoxy.EpoxyController
import com.dlgsoft.mcinema.R
import com.dlgsoft.mcinema.data.db.relations.MovieWithGenres
import com.dlgsoft.mcinema.data.db.relations.ReviewsWithTotal
import com.dlgsoft.mcinema.ui.views.buttonView
import com.dlgsoft.mcinema.ui.views.movieDetailView
import com.dlgsoft.mcinema.ui.views.movieReviewView
import com.dlgsoft.mcinema.ui.views.spacer

class MovieController(private val clickListener: (id: Long) -> Unit) : EpoxyController() {

    private var showReviewsClicked = false

    var movie: MovieWithGenres? = null
        set(value) {
            field = value
            requestModelBuild()
        }

    var rwt: ReviewsWithTotal? = null
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        movie?.let {
            movieDetailView {
                id(it.movie.id)
                title(it.movie.title)
                votes(it.movie.votes.toString())
                genres(it.genres?.joinToString(", ") { it.genre })
                poster(it.movie.posterUrl)
                backdrop(it.movie.backdropUrl)
                avg(it.movie.voteAvg)
            }

            if (!showReviewsClicked) {
                spacer {
                    id("spacer")
                    height(R.dimen.space_48dp)
                }
                buttonView {
                    id("show_reviews")
                    listener {
                        showReviewsClicked = true
                        clickListener.invoke(it.movie.id)
                    }
                }
            } else {
                rwt?.let {
                    print(it.movieReviews.totalReviews)
                }

                rwt?.reviews?.forEach {
                    movieReviewView {
                        id(it.id)
                        author(it.author)
                        authorPhoto(it.avatarUrl)
                        review(it.content)
                    }
                }
            }
        }
    }
}
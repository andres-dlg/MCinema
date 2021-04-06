package com.dlgsoft.mcinema.ui.views

import android.content.Context
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.dlgsoft.mcinema.R
import com.dlgsoft.mcinema.utils.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class MovieDetailView(context: Context) : FrameLayout(context) {

    @Inject
    lateinit var imageLoader: ImageLoader

    private val poster: ImageView by lazy { findViewById(R.id.poster) }
    private val backdrop: ImageView by lazy { findViewById(R.id.backdrop) }
    private val title: TextView by lazy { findViewById(R.id.title) }
    private val votes: TextView by lazy { findViewById(R.id.votes) }
    private val genres: TextView by lazy { findViewById(R.id.genres) }
    private val votesAvg: AvgTextView by lazy { findViewById(R.id.avg_score) }
    private val stars: StarsView by lazy { findViewById(R.id.stars) }

    @TextProp
    fun setTitle(text: CharSequence?) {
        title.text = text
    }

    @TextProp
    fun setVotes(text: CharSequence?) {
        votes.text = text
    }

    @TextProp
    fun setGenres(text: CharSequence?) {
        genres.text = text
    }

    @ModelProp
    fun setPoster(posterUrl: String?) {
        imageLoader.loadImage(poster, posterUrl, "w342")
    }

    @ModelProp
    fun setBackdrop(backdropUrl: String?) {
        imageLoader.loadImage(backdrop, backdropUrl, "w300")
    }

    @ModelProp
    fun setAvg(avg: Double) {
        votesAvg.apply {
            setScore(avg.toString())
            setTextColor(R.color.ruby)
            setAvgBackground(null)
            stars.setScore(avg)
        }
    }

    init {
        inflate(context, R.layout.view_movie_detail, this)
    }
}
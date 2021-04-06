package com.dlgsoft.mcinema.ui.views

import android.content.Context
import android.text.TextUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.dlgsoft.mcinema.R
import com.dlgsoft.mcinema.utils.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class MovieReviewView(context: Context) : LinearLayout(context) {

    @Inject
    lateinit var imageLoader: ImageLoader

    private var isCollapsed = INITIAL_IS_COLLAPSED

    private val card: CardView by lazy { findViewById(R.id.card) }
    private val author: TextView by lazy { findViewById(R.id.author) }
    private val review: TextView by lazy { findViewById(R.id.review) }
    private val avatar: ImageView by lazy { findViewById(R.id.avatar) }

    @ModelProp
    fun setAuthorPhoto(url: String) {
        imageLoader.loadImage(avatar, url, "w185", true)
    }

    @TextProp
    fun setAuthor(text: CharSequence?) {
        author.text = text
    }

    @TextProp
    fun setReview(text: CharSequence?) {
        review.text = text
    }

    init {
        inflate(context, R.layout.view_movie_review, this)
        card.setOnClickListener {
            TransitionManager.beginDelayedTransition(card, AutoTransition())
            if (isCollapsed) {
                review.maxLines = Int.MAX_VALUE
                review.ellipsize = null
            } else {
                review.maxLines = MAX_LINES_COLLAPSED
                review.ellipsize = TextUtils.TruncateAt.END
            }
            isCollapsed = !isCollapsed
        }
    }

    companion object {
        private const val MAX_LINES_COLLAPSED = 3
        private const val INITIAL_IS_COLLAPSED = true
    }
}
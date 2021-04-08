package com.dlgsoft.mcinema.ui.views

import android.content.Context
import android.text.SpannableString
import android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.dlgsoft.mcinema.R
import com.dlgsoft.mcinema.utils.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class MovieItemView(context: Context) : FrameLayout(context) {

    private val poster: ImageView by lazy { findViewById(R.id.poster) }
    private val title: TextView by lazy { findViewById(R.id.title) }
    private val year: TextView by lazy { findViewById(R.id.year) }
    private val adults: TextView by lazy { findViewById(R.id.adult) }
    private val avg: AvgTextView by lazy { findViewById(R.id.vote_avg) }

    @Inject
    lateinit var imageLoader: ImageLoader

    @TextProp
    fun setTitle(text: CharSequence?) {
        this.title.text = text
    }

    @TextProp
    fun setYear(text: CharSequence?) {
        this.year.text = text
    }

    @TextProp
    fun setAvg(text: CharSequence?) {
        avg.apply {
            if (text.isNullOrBlank()) {
                isVisible = false
            } else {
                isVisible = true
                setScore(text.toString())
            }
        }
    }

    @ModelProp
    fun setIsForAdults(isForAdults: Boolean) {
        adults.isVisible = isForAdults
    }

    @ModelProp
    fun setImageUrl(imageUrl: String?) {
        imageLoader.loadImage(poster, imageUrl, "w342")
    }

    @CallbackProp
    fun setListener(listener: (() -> Unit)?) {
        rootView.setOnClickListener {
            listener?.invoke()
        }
    }

    init {
        inflate(context, R.layout.view_movie_item, this)
        clipChildren = false
        clipToPadding = false
    }
}
package com.dlgsoft.mcinema.ui.views

import android.content.Context
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.dlgsoft.mcinema.R
import dagger.hilt.android.AndroidEntryPoint

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class MovieTotalReviewsView(context: Context) : FrameLayout(context) {

    private val total: TextView by lazy { findViewById(R.id.total) }

    @ModelProp
    fun setTotal(totalReviews: Int) {
        total.text = context.getString(R.string.total_reviews, totalReviews)
    }

    init {
        inflate(context, R.layout.view_movie_total_reviews, this)
    }
}
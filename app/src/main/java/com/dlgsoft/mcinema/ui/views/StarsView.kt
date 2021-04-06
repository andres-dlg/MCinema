package com.dlgsoft.mcinema.ui.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.dlgsoft.mcinema.R
import kotlin.math.floor


class StarsView : FrameLayout {

    private val root: LinearLayout by lazy { findViewById(R.id.root) }

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    private fun initView() {
        inflate(context, R.layout.stars_view, this)
    }

    fun setScore(score: Double) {
        val filledStars = floor(score / 2).toInt()
        val outlineStars = 5 - filledStars

        repeat(filledStars) {
            addStar(R.drawable.star)
        }
        repeat(outlineStars) {
            addStar(R.drawable.star_outline)
        }
    }

    private fun addStar(starResId: Int) {
        // Initialize a new ImageView widget
        val iv = ImageView(context)

        // Set an image for ImageView
        iv.setImageDrawable(ContextCompat.getDrawable(context, starResId))

        // Create layout parameters for ImageView
        val lp = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        // Add layout parameters to ImageView
        iv.layoutParams = lp

        // Finally, add the ImageView to layout
        root.addView(iv)
    }
}
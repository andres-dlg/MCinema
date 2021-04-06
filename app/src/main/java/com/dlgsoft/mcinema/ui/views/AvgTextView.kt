package com.dlgsoft.mcinema.ui.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.dlgsoft.mcinema.R

class AvgTextView : FrameLayout {

    private val voteAvgFirst: TextView by lazy { findViewById(R.id.vote_avg_first) }
    private val voteAvgSecond: TextView by lazy { findViewById(R.id.vote_avg_second) }
    private val root: ConstraintLayout by lazy { findViewById(R.id.root) }

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
        inflate(context, R.layout.textview_avg, this)
    }

    fun setScore(score: String) {
        val first = score.split(".")[0]
        val second = score.split(".")[1]
        voteAvgFirst.text = first
        voteAvgSecond.text = if (second != "0") {
            ".$second"
        } else {
            ""
        }
    }

    fun setTextColor(colorResId: Int) {
        voteAvgFirst.setTextColor(ContextCompat.getColor(context, colorResId))
        voteAvgSecond.setTextColor(ContextCompat.getColor(context, colorResId))
    }

    fun setAvgBackground(backgroundResId: Int?) {
        if (backgroundResId != null) {
            root.setBackgroundResource(backgroundResId)
        } else {
            root.background = null
        }
    }

}
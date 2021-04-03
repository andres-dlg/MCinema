package com.dlgsoft.mcinema.ui.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import com.dlgsoft.mcinema.R

class AvgTextView : FrameLayout {

    private val voteAvgFirst: TextView by lazy { findViewById(R.id.vote_avg_first) }
    private val voteAvgSecond: TextView by lazy { findViewById(R.id.vote_avg_second) }

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
        voteAvgSecond.text = ".$second"
    }

}
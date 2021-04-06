package com.dlgsoft.mcinema.ui.views

import android.content.Context
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.dlgsoft.mcinema.R
import dagger.hilt.android.AndroidEntryPoint

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ButtonView(context: Context) : FrameLayout(context) {

    private val button: AppCompatButton by lazy { findViewById(R.id.button) }

    @CallbackProp
    fun setListener(listener: (() -> Unit)?) {
        button.setOnClickListener { listener?.invoke() }
    }

    init {
        inflate(context, R.layout.view_button, this)
    }
}
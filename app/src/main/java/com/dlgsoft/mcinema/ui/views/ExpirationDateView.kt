package com.dlgsoft.mcinema.ui.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.LinearLayout
import com.airbnb.epoxy.*
import com.dlgsoft.mcinema.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ExpirationDateView(context: Context) : LinearLayout(context) {

    private var isUserInteracting: Boolean = false
    private val days: EditText by lazy { findViewById(R.id.days) }
    private val hours: EditText by lazy { findViewById(R.id.hours) }
    private val minutes: EditText by lazy { findViewById(R.id.minutes) }

    private val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Expected
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (isUserInteracting) {
                listener?.invoke(
                    if (days.text.isNullOrBlank()) 0 else days.text.toString().toInt(),
                    if (hours.text.isNullOrBlank()) 0 else hours.text.toString().toInt(),
                    if (minutes.text.isNullOrBlank()) 0 else minutes.text.toString().toInt()
                )
            }
        }

        override fun afterTextChanged(s: Editable?) {
            // Expected
        }
    }

    private var listener: ((Int, Int, Int) -> Unit)? = null

    @ModelProp
    fun setDays(days: Int) {
        this.days.setText(days.toString())
    }

    @ModelProp
    fun setHours(hours: Int) {
        this.hours.setText(hours.toString())
    }

    @ModelProp
    fun setMinutes(minutes: Int) {
        this.minutes.setText(minutes.toString())
    }

    @CallbackProp
    fun setListener(listener: ((Int, Int, Int) -> Unit)?) {
        this.listener = listener
    }

    @AfterPropsSet
    fun finishSetup() {
        isUserInteracting = true
    }

    init {
        inflate(context, R.layout.view_expiration_date, this)
        days.addTextChangedListener(textChangeListener)
        hours.addTextChangedListener(textChangeListener)
        minutes.addTextChangedListener(textChangeListener)
    }
}
package com.dlgsoft.mcinema.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color.TRANSPARENT
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager.LayoutParams
import android.view.WindowManager.LayoutParams.MATCH_PARENT
import android.widget.Button
import android.widget.ImageButton
import com.airbnb.epoxy.EpoxyRecyclerView
import com.dlgsoft.mcinema.R

open class ExpirationDateDialog(
    context: Context
) : Dialog(context) {

    private val customDialogController by lazy { ExpirationDateDialogController() }

    private val applyBtn: Button by lazy { findViewById(R.id.btn_apply) }
    private val cancelBtn: Button by lazy { findViewById(R.id.btn_cancel) }
    private val closeBtn: ImageButton by lazy { findViewById(R.id.btn_close) }
    private val container: EpoxyRecyclerView by lazy { findViewById(R.id.container) }

    fun init(expirationDate: ExpirationDate) {

        setContentView(R.layout.dialog_filters)

        window?.apply {
            setBackgroundDrawable(ColorDrawable(TRANSPARENT))
            attributes = LayoutParams().apply {
                copyFrom(window?.attributes)
                width = MATCH_PARENT
                height = MATCH_PARENT
            }
        }

        container.setController(customDialogController)

        customDialogController.apply {
            this.expirationDate = expirationDate
            requestModelBuild()
        }

        cancelBtn.setOnClickListener { dismiss() }
        closeBtn.setOnClickListener { dismiss() }
    }

    fun setApplyButtonListener(listener: (Long) -> Unit) {
        applyBtn.setOnClickListener {
            listener.invoke(customDialogController.getExpirationTimeInMillis())
        }
    }
}
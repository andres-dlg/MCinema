package com.dlgsoft.mcinema.ui.dialogs

import com.airbnb.epoxy.EpoxyController
import com.dlgsoft.mcinema.ui.views.expirationDateView

class ExpirationDateDialogController : EpoxyController() {

    lateinit var expirationDate: ExpirationDate

    override fun buildModels() {
        expirationDateView {
            id("expiration_day")
            days(expirationDate.days)
            hours(expirationDate.hours)
            minutes(expirationDate.minutes)
            listener { days, hours, minutes ->
                expirationDate.setData(days, hours, minutes)
            }
        }
    }

    fun getExpirationTimeInMillis(): Long {
        return expirationDate.millis
    }
}
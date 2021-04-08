package com.dlgsoft.mcinema.ui.dialogs

class ExpirationDate(expirationDateInMillis: Long) {
    var days = 0
    var hours = 0
    var minutes = 0
    var millis = 0L

    init {
        val seconds: Long = expirationDateInMillis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        this.millis = expirationDateInMillis
        this.days = days.toInt()
        this.hours = (hours % 24).toInt()
        this.minutes = (minutes % 60).toInt()
    }


    fun setData(days: Int, hours: Int, minutes: Int) {
        this.days = days
        this.hours = hours
        this.minutes = minutes
        calculateTimeInMillis()
    }

    private fun calculateTimeInMillis() {
        millis =
            ((minutes * 60 * 1000) + (hours * 60 * 60 * 1000) + (days * 24 * 60 * 60 * 1000)).toLong()
    }
}
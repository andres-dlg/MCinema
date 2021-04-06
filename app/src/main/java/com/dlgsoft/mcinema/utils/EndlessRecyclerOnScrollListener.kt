package com.dlgsoft.mcinema.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EndlessRecyclerOnScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val callback: () -> Unit,
    private val itemsUntilInvokeCallback: Int,
    var listSize: Int
) : RecyclerView.OnScrollListener() {

    var isLoading: Boolean = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (!isLoading) {
            val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
            if (dy > 0 && lastVisibleItemPosition == listSize - itemsUntilInvokeCallback) {
                callback.invoke()
                isLoading = true
            }
        }
    }
}
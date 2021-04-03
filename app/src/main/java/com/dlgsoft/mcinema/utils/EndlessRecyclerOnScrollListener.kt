package com.dlgsoft.mcinema.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EndlessRecyclerOnScrollListener(
    private val layoutManager: GridLayoutManager,
    private val callback: () -> Unit,
    var listSize: Int,
) :
    RecyclerView.OnScrollListener() {

    var isLoading: Boolean = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (!isLoading) {
            val index = layoutManager.findLastCompletelyVisibleItemPosition()
            if (index == listSize - 11) {
                callback.invoke()
                isLoading = true
            }
        }
    }
}
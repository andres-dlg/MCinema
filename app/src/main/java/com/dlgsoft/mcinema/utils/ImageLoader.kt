package com.dlgsoft.mcinema.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.dlgsoft.mcinema.BuildConfig.BASE_URL_IMAGE
import com.dlgsoft.mcinema.R
import javax.inject.Inject

class ImageLoader @Inject constructor(
    private val context: Context,
) {

    fun loadImage(imageView: ImageView, imageUrl: String?, size: String) {
        if (imageUrl != null) {
            val glideUrl = GlideUrl("$BASE_URL_IMAGE/$size$imageUrl")
            Glide.with(context)
                .load(glideUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(imageView)
        } else {
            Glide.with(context)
                .load(R.drawable.placeholder)
                .into(imageView)
        }
    }
}
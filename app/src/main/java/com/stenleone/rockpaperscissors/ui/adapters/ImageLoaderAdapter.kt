package com.stenleone.rockpaperscissors.ui.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.stenleone.stanleysfilm.util.glide.GlideApp

@BindingAdapter("app:load_image")
fun loadImage(view: ImageView, url: String?) {
    url?.let {
        GlideApp
            .with(view)
            .load(it)
            .into(view)
    }
}

@BindingAdapter("app:load_image")
fun loadImage(view: ImageView, res: Int?) {
    res?.let {
        GlideApp
            .with(view)
            .load(res)
            .into(view)
    }
}
package com.stenleone.rockpaperscissors.ui.adapters

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.utils.constants.RPS
import com.stenleone.stanleysfilm.util.glide.GlideApp

@BindingAdapter("app:set_rps", "app:set_user_step")
fun setRPS(view: ImageView, rps: RPS?, userStep: Boolean) {
    if (rps != null) {
        if (userStep) {
            view.visibility = View.VISIBLE
            when (rps) {
                RPS.PAPER -> {
                    GlideApp
                        .with(view)
                        .load(R.drawable.paper)
                        .into(view)
                }
                RPS.ROCK -> {
                    GlideApp
                        .with(view)
                        .load(R.drawable.rock)
                        .into(view)
                }
                RPS.SCISSORS -> {
                    GlideApp
                        .with(view)
                        .load(R.drawable.scissors)
                        .into(view)
                }
            }
        } else {
            view.visibility = View.INVISIBLE
        }
    } else {
        view.visibility = View.INVISIBLE
    }
}
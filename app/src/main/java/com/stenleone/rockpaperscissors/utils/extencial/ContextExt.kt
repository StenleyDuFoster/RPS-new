package com.stenleone.rockpaperscissors.utils.extencial

import android.app.Activity
import android.content.Intent

fun Activity.share(title: String, body: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    val shareBody = body
    intent.putExtra(Intent.EXTRA_TEXT, shareBody)
    startActivity(Intent.createChooser(intent, title))
}
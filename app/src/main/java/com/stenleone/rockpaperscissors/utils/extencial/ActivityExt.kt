package com.stenleone.stanleysfilm.util.extencial

import android.R
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.view.inputmethod.InputMethodManager

//fun Activity.copyToClipBoard(copyText: String?) {
//    val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//    val clip = ClipData.newPlainText(BaseActivity.COPY_LABEL, copyText)
//    clipboard.setPrimaryClip(clip)
//    Toast.makeText(this, getString(R.string.content_copy_to_clip_board), Toast.LENGTH_SHORT).show()
//}

fun Activity.getOrientation(): Int {
    return if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Configuration.ORIENTATION_PORTRAIT
    } else {
        Configuration.ORIENTATION_LANDSCAPE
    }
}

fun Activity.hideKeyboard() {
    val view: View = this.findViewById(R.id.content)
    if (view != null) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
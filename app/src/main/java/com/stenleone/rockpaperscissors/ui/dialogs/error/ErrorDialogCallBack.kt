package com.stenleone.rockpaperscissors.ui.dialogs.error

interface ErrorDialogCallBack {

    fun errorDialogOkClick(type: Int)
    fun errorDialogRetryClick(type: Int)
}
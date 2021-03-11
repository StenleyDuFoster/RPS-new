package com.stenleone.rockpaperscissors.model.general

import com.stenleone.stanleysfilm.model.entity.RequestError

sealed class DataState<out R> {
    data class Success<out T>(val data: T): DataState<T>()
    data class Error(val exception: RequestError): DataState<Nothing>()
}
package com.stenleone.rockpaperscissors.model.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("name")
    var name: String?,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("win")
    var win: Int = 0,
    @SerializedName("lose")
    var lose: Int = 0,
    @SerializedName("image")
    var image: String? = null
): Parcelable {
    constructor() : this (null,"","",0,0,null)

    fun mapToGameUser(): GameUser {
        return GameUser(
            name,
            win,
            lose,
            image,
            arrayListOf()
        )
    }
}

package com.stenleone.rockpaperscissors.model.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Room(
    val name: String,
    val password: String? = null,
    @SerializedName("player_count")
    val playerCount: Int,
    val games: Int,
    @SerializedName("date_create")
    val date_create: String,
    val players: ArrayList<GameUser>
) : Parcelable {
    constructor() : this("", null, 0, 0, "", arrayListOf())
}
package com.stenleone.rockpaperscissors.model.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Room(
    @SerializedName("name")
    val name: String,
    @SerializedName("password")
    val password: String? = null,
    @SerializedName("player_count")
    val playerCount: Int,
    @SerializedName("games")
    val games: Int,
    @SerializedName("round")
    var round: Int,
    @SerializedName("date_create")
    val date_create: String,
    @SerializedName("players")
    var players: Map<String, GameUser>
) : Parcelable {
    constructor() : this("", null, 0, 0, 1, "", mapOf())
//
//    constructor(
//        name: String, password: String? = null, playerCount: Int, games: Int, date_create: String, players: ArrayList<GameUser>
//    ) : this("", null, 0, 0, "", arrayListOf())
}
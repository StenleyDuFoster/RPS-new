package com.stenleone.rockpaperscissors.model.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.stenleone.rockpaperscissors.utils.constants.RPS
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GameUser(
    @SerializedName("name")
    var name: String?,
    @SerializedName("win")
    var win: Int = 0,
    @SerializedName("lose")
    var lose: Int = 0,
    @SerializedName("image")
    var image: String? = null,
    @SerializedName("steps")
    var steps: ArrayList<RPS>,
) : Parcelable {
    constructor() : this(null, 0, 0, null, arrayListOf())
}

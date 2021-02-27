package com.stenleone.rockpaperscissors.model.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GameData(
    val round: Int
): Parcelable {
    constructor() : this(0)
}

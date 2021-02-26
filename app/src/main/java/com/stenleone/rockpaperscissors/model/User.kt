package com.stenleone.rockpaperscissors.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
    @SerializedName("name")
    @Expose
    val name: String?,
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("password")
    @Expose
    val password: String,
    @SerializedName("win")
    @Expose
    var win: Int = 0,
    @SerializedName("lose")
    @Expose
    var lose: Int = 0,
    @SerializedName("image")
    @Expose
    var image: String? = null
): Parcelable {
    constructor() : this ("","","",0,0,"")
}

package com.stenleone.rockpaperscissors.managers.network

import com.google.firebase.database.FirebaseDatabase
import com.stenleone.rockpaperscissors.managers.network.base.BaseNetworkManager
import javax.inject.Inject

class PlayerRoomManager @Inject constructor() : BaseNetworkManager() {

    companion object {
        private const val ROOM_DB = "rooms-new"
//        private const val
    }

    private val realTimeDB by lazy { FirebaseDatabase.getInstance() }

//    private val
}
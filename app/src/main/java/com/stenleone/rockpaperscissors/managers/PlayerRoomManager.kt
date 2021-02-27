package com.stenleone.rockpaperscissors.managers

import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class PlayerRoomManager @Inject constructor() {

    companion object {
        private const val ROOM_DB = "rooms-new"
//        private const val
    }

    private val realTimeDB by lazy { FirebaseDatabase.getInstance() }

//    private val
}
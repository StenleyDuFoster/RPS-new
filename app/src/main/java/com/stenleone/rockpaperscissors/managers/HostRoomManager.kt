package com.stenleone.rockpaperscissors.managers

import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class HostRoomManager @Inject constructor() {

    companion object {
        private const val ROOM_DB = "rooms-new"
        private const val
    }

    private val realTimeDB by lazy { FirebaseDatabase.getInstance() }
}
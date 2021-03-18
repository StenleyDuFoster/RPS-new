package com.stenleone.rockpaperscissors.managers.network.realTime

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.stenleone.rockpaperscissors.managers.network.base.BaseNetworkManager
import com.stenleone.rockpaperscissors.model.network.GameUser
import com.stenleone.rockpaperscissors.model.network.Room
import javax.inject.Inject

class PlayerRoomManager @Inject constructor() : BaseNetworkManager() {

    companion object {
        private const val ROOM_DB = "rooms-new"
    }

    private val realTimeDB by lazy { FirebaseDatabase.getInstance() }

    fun observe(room: Room, update: (Room) -> Unit, failure: (String) -> Unit) {
        realTimeDB.getReference(ROOM_DB).child(room.name).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val newData = try {
                    snapshot.getValue(Room::class.java)
                } catch (e: Exception) {
                    null
                }
                newData?.let(update)
            }

            override fun onCancelled(error: DatabaseError) {
                failure(error.message)
            }

        })
    }

    fun updateRoomPlayer(roomName: String, user: GameUser, success: () -> Unit, failure: (String) -> Unit) {
        val mapUpdate = HashMap<String, GameUser>()
        mapUpdate.put(user.name ?: "", user)
        realTimeDB.getReference(ROOM_DB).child(roomName).child("players").updateChildren(mapUpdate as Map<String, Any>)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    success()
                }
            }
            .addOnFailureListener {
                failure(it.message ?: "")
            }
    }
}
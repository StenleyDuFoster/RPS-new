package com.stenleone.rockpaperscissors.managers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.stenleone.rockpaperscissors.model.network.GameUser
import com.stenleone.rockpaperscissors.model.network.Room
import javax.inject.Inject

class HostRoomManager @Inject constructor() {

    companion object {
        private const val ROOM_DB = "rooms-new"
    }

    private val realTimeDB by lazy { FirebaseDatabase.getInstance() }

    fun createRoom(room: Room, success: () -> Unit, failure: (String) -> Unit) {
        realTimeDB.getReference(ROOM_DB).child(room.name).setValue(
            room
        )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    success()
                }
            }
            .addOnFailureListener {
                failure(it.message ?: "")
            }
    }

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

    fun removeRoom(roomName: String, success: () -> Unit, failure: (String) -> Unit) {
        realTimeDB.getReference(ROOM_DB).child(roomName).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    success()
                }
            }
            .addOnFailureListener {
                failure(it.message ?: "")
            }

    }

    fun updateRoomPlayer(room: Room, success: () -> Unit, failure: (String) -> Unit) {
        val mapUpdate = HashMap<String, Map<String, GameUser>>()
        mapUpdate.put("players", room.players)
        realTimeDB.getReference(ROOM_DB).child(room.name).updateChildren(mapUpdate as Map<String, Any>)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    success()
                }
            }
            .addOnFailureListener {
                failure(it.message ?: "")
            }
    }

    fun updateRoomRound(room: Room, success: () -> Unit, failure: (String) -> Unit) {
        val mapUpdate = HashMap<String, Int>()
        mapUpdate.put("round", room.round)
        realTimeDB.getReference(ROOM_DB).child(room.name).updateChildren(mapUpdate as Map<String, Any>)
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
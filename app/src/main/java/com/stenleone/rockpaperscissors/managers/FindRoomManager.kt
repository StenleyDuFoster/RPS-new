package com.stenleone.rockpaperscissors.managers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.annotations.SerializedName
import com.stenleone.rockpaperscissors.model.network.GameUser
import com.stenleone.rockpaperscissors.model.network.Room
import javax.inject.Inject

class FindRoomManager @Inject constructor() {

    companion object {
        private const val ROOM_DB = "rooms-new"
    }

    private val realTimeDB by lazy { FirebaseDatabase.getInstance() }

    fun observeRooms(update: (ListRooms) -> Unit, failure: (String) -> Unit) {
        realTimeDB.getReference(ROOM_DB).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val newData = try {
                    snapshot.getValue(ListRooms::class.java)
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

    data class ListRooms(
        val o: Room?
    ) {
        constructor() : this(null)
    }
}
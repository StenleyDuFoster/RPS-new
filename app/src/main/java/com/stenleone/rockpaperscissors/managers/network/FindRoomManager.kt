package com.stenleone.rockpaperscissors.managers.network

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.stenleone.rockpaperscissors.model.network.Room
import javax.inject.Inject


class FindRoomManager @Inject constructor() {

    companion object {
        private const val ROOM_DB = "rooms-new"
    }

    private val realTimeDB by lazy { FirebaseDatabase.getInstance() }

    fun observeRooms(update: (ArrayList<Room>) -> Unit, failure: (String) -> Unit) {
        realTimeDB.getReference(ROOM_DB).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val newData = try {
                    val local = snapshot.value as HashMap<String, HashMap<String, Any>>
                    val rooms = arrayListOf<Room>()

                    local.values.forEach {
                        val gson = Gson()
                        val jsonElement = gson.toJsonTree(it)
                        val pojo: Room = gson.fromJson(jsonElement, Room::class.java)
                        rooms.add(pojo)
                    }

                    rooms
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
}
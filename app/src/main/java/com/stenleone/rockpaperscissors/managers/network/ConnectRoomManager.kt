package com.stenleone.rockpaperscissors.managers.network

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.stenleone.rockpaperscissors.managers.network.base.BaseNetworkManager
import com.stenleone.rockpaperscissors.model.network.GameUser
import com.stenleone.rockpaperscissors.model.network.Room
import com.stenleone.rockpaperscissors.model.network.User
import javax.inject.Inject

class ConnectRoomManager @Inject constructor() : BaseNetworkManager() {

    companion object {
        private const val ROOM_DB = "rooms-new"
    }

    private val realTimeDB by lazy { FirebaseDatabase.getInstance() }

    fun connect(user: User, room: Room, password: String, success: () -> Unit, failureGeneral: (String) -> Unit, failureNotAviable: () -> Unit) {

        getRoom(room, password, { newRoom ->

            val addPlayerMap = HashMap<String, HashMap<String, GameUser>>()
            val usersMap = HashMap(newRoom.players)
            usersMap.put(
                user.name ?: user.email.replace(Regex("\\$%.@"), ""), GameUser(
                    user.name ?: user.email.replace(Regex("\\$%.@"), ""),
                    0,
                    0,
                    null,
                    arrayListOf()
                )
            )
            addPlayerMap.put("players", usersMap)

            realTimeDB.getReference(ROOM_DB).child(room.name).updateChildren(addPlayerMap as Map<String, Any>)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        success()
                    }
                }
                .addOnFailureListener {
                    failureGeneral(it.message ?: "")
                }

        }, failureGeneral, failureNotAviable)

    }

    fun connect(user: User, room: Room, success: () -> Unit, failureGeneral: (String) -> Unit, failureNotAviable: () -> Unit) {

        getRoom(room, { newRoom ->

            val addPlayerMap = HashMap<String, HashMap<String, GameUser>>()
            val usersMap = HashMap(newRoom.players)
            usersMap.put(
                user.name ?: user.email.replace(Regex("\\$%.@"), ""), GameUser(
                    user.name ?: user.email.replace(Regex("\\$%.@"), ""),
                    0,
                    0,
                    null,
                    arrayListOf()
                )
            )
            addPlayerMap.put("players", usersMap)

            realTimeDB.getReference(ROOM_DB).child(room.name).updateChildren(addPlayerMap as Map<String, Any>)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        success()
                    }
                }
                .addOnFailureListener {
                    failureGeneral(it.message ?: "")
                }

        }, failureGeneral, failureNotAviable)

    }

    private fun getRoom(room: Room, password: String, update: (Room) -> Unit, failureGeneral: (String) -> Unit, failureNotAviable: () -> Unit) {

        var listener: ValueEventListener? = null
        listener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val newData = try {
                    snapshot.getValue(Room::class.java)
                } catch (e: Exception) {
                    null
                }

                newData?.let { updateData ->

                    if (updateData.players.values.size < updateData.playerCount) {

                        if (updateData.password == password) {
                            update(updateData)
                            listener?.let { realTimeDB.getReference(ROOM_DB).child(room.name).removeEventListener(it) }
                        } else {
                            failureNotAviable()
                        }

                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {
                failureGeneral(error.message)
            }

        }

        realTimeDB.getReference(ROOM_DB).child(room.name).addValueEventListener(listener)

    }

    private fun getRoom(room: Room, update: (Room) -> Unit, failureGeneral: (String) -> Unit, failureNotAviable: () -> Unit) {

        var listener: ValueEventListener? = null
        listener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val newData = try {
                    snapshot.getValue(Room::class.java)
                } catch (e: Exception) {
                    null
                }

                newData?.let { updateData ->

                    if (updateData.players.values.size < updateData.playerCount) {

                        update(updateData)
                        listener?.let { realTimeDB.getReference(ROOM_DB).child(room.name).removeEventListener(it) }
                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {
                failureGeneral(error.message)
            }

        }

        realTimeDB.getReference(ROOM_DB).child(room.name).addValueEventListener(listener)

    }
}
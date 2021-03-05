package com.stenleone.rockpaperscissors.ui.fragments.hostPlayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stenleone.rockpaperscissors.managers.HostRoomManager
import com.stenleone.rockpaperscissors.managers.ProfileCloudFirestoreManager
import com.stenleone.rockpaperscissors.model.network.GameUser
import com.stenleone.rockpaperscissors.model.network.Room
import com.stenleone.rockpaperscissors.utils.constants.RPS
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HostPlayerViewModel @Inject constructor(
    private val hostRoomManager: HostRoomManager,
    private val profileCloudFirestoreManager: ProfileCloudFirestoreManager
) : ViewModel() {

    private val roomDataControll = TODO()
    val roomData = MutableLiveData<Room>()
    val error = MutableLiveData<String>()

    fun setupRoom(room: Room) {
        if (roomData.value == null) {
            roomData.postValue(room)

            addHostPlayer(room)
        }
    }

    fun createStep(step: RPS) {
        roomData.value?.let {
            val newRoom = it
            newRoom.players.firstOrNull()?.steps?.add(step)
            hostRoomManager.updateRoomPlayer(newRoom, {

            }, {
                error.postValue(it)
            })
        }
    }

    private fun observeRoomData(room: Room) {
        hostRoomManager.observe(room, {
            roomData.postValue(it)
        }, {
            error.postValue(it)
        })
    }

    private fun addHostPlayer(room: Room) {
        profileCloudFirestoreManager.getProfile({
            val newRoom = room
            val gameUser = GameUser()
            gameUser.apply {
                name = it.name ?: it.email
                win = it.win
                lose = it.lose
                steps = arrayListOf()
                image = it.image
            }
            newRoom.players.add(gameUser)

            hostRoomManager.createRoom(newRoom, {
                observeRoomData(newRoom)
            }, {
                error.postValue(it)
            })
        }, {
            error.postValue(it)
        })
    }
}
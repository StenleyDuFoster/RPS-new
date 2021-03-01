package com.stenleone.rockpaperscissors.ui.fragments.hostPlayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stenleone.rockpaperscissors.managers.HostRoomManager
import com.stenleone.rockpaperscissors.model.network.Room
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HostPlayerViewModel @Inject constructor(
    private val hostRoomManager: HostRoomManager
) : ViewModel() {

    val roomData = MutableLiveData<Room>()
    val error = MutableLiveData<String>()

    fun setupRoom(room: Room) {
        if (roomData.value == null) {
            roomData.postValue(room)
            hostRoomManager.createRoom(room, {
                observeRoomData(room)
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
}
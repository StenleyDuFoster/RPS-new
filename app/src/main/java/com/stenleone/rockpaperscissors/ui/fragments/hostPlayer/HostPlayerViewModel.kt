package com.stenleone.rockpaperscissors.ui.fragments.hostPlayer

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.stenleone.rockpaperscissors.managers.network.realTime.HostRoomManager
import com.stenleone.rockpaperscissors.managers.network.fireStore.ProfileCloudFirestoreManager
import com.stenleone.rockpaperscissors.model.network.GameUser
import com.stenleone.rockpaperscissors.model.network.Room
import com.stenleone.rockpaperscissors.utils.constants.RPS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HostPlayerViewModel @Inject constructor(
    private val hostRoomManager: HostRoomManager,
    private val profileCloudFirestoreManager: ProfileCloudFirestoreManager
) : ViewModel() {

    var name: String = ""
    lateinit var roomControl: Room

    val roomData = MutableLiveData<Room>()
    val error = MutableLiveData<String>()
    val lockButtons = MutableLiveData<Boolean>()
    val round = MutableLiveData<Int>()

    fun setupRoom(room: Room) {
        if (roomData.value == null) {

            addHostPlayer(room)
        }
    }

    fun createStep(step: RPS) {
        Log.v("112233", "change gamers ${roomControl.players.size}")
        val newRoom = roomControl
        newRoom.players.get(name)?.steps?.add(step)
        hostRoomManager.updateRoomPlayer(newRoom, {

        }, {
            error.postValue(it)
        })
    }

    private fun observeRoomData(room: Room) {
        hostRoomManager.observe(room, {

            savePlayerControl(it)

            if (checkPlayersFinishRound(it)) {
                lockButtons.postValue(false)
            }

            updateRound(it.round, it)

        }, {
            error.postValue(it)
        })
    }

    private fun checkPlayersFinishRound(room: Room): Boolean {
        var finish = true

        room.players.values.forEach {
            if (it.steps.size < room.round) {
                finish = false
            }
        }

        if (room.players.size != room.playerCount) {
            finish = false
        }

        if (finish) {
            val newRoom = room
            newRoom.round += 1
            updateRound(newRoom)
        }

        return finish
    }

    private fun updateRound(room: Room) {
        hostRoomManager.updateRoomRound(room, {

        }, {
            error.postValue(it)
        })
    }

    private fun savePlayerControl(room: Room) {
        viewModelScope.launch {
            withContext(Main) {
                roomControl = room
                roomData.postValue(room)
            }
        }
    }

    private fun updateRound(oldRound: Int?, room: Room) {
        if (oldRound ?: 0 < room.round) {
            viewModelScope.launch {
                delay(3000)
                round.postValue(room.round)
            }
        }
    }

    private fun addHostPlayer(room: Room) {
        profileCloudFirestoreManager.getProfile({
            val newRoom = room
            name = it.name ?: it.email.replace(Regex("\\$%.@"), "")
            val gameUser = GameUser(
                name = name,
                win = it.win,
                lose = it.lose,
                steps = arrayListOf(),
                image = it.image
            )
            newRoom.players = mapOf(Pair(name, gameUser))

            Log.v("112233", "add gamers ${newRoom.players?.size}")
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
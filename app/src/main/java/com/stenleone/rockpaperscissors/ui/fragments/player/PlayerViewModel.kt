package com.stenleone.rockpaperscissors.ui.fragments.player

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stenleone.rockpaperscissors.managers.network.fireStore.ProfileCloudFirestoreManager
import com.stenleone.rockpaperscissors.managers.network.realTime.PlayerRoomManager
import com.stenleone.rockpaperscissors.model.network.GameUser
import com.stenleone.rockpaperscissors.model.network.Room
import com.stenleone.rockpaperscissors.utils.constants.RPS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerRoomManager: PlayerRoomManager,
    private val profileCloudFirestoreManager: ProfileCloudFirestoreManager
) : ViewModel() {

    var playerName: String = ""
    private var roomName: String = ""

    private lateinit var userControl: GameUser

    val roomData = MutableLiveData<Room>()
    val error = MutableLiveData<String>()
    val lockButtons = MutableLiveData<Boolean>()

    fun setupRoom(room: Room) {
        if (roomData.value == null) {

            addPlayer(room)
        }
    }

    private fun addPlayer(room: Room) {
        profileCloudFirestoreManager.getProfile({
            val newRoom = room
            playerName = it.name ?: it.email.replace(Regex("\\$%.@"), "")
            val gameUser = GameUser(
                name = playerName,
                win = it.win,
                lose = it.lose,
                steps = arrayListOf(),
                image = it.image
            )
            newRoom.players = mapOf(Pair(playerName, gameUser))
            roomName = room.name

            Log.v("112233", "add gamers ${newRoom.players?.size}")
            observeRoomData(newRoom)
        }, {
            error.postValue(it)
        })
    }

    private fun observeRoomData(room: Room) {
        playerRoomManager.observe(room, {

            savePlayerControl(it)

            if (checkRoomStartNewRound(it)) {
                lockButtons.postValue(false)
            }
        }, {
            error.postValue(it)
        })
    }

    private fun checkRoomStartNewRound(room: Room): Boolean {
        var finish = true

        if (userControl.steps.size >= room.round) {
            finish = false
        }

        if (room.players.size != room.playerCount) {
            finish = false
        }

        return finish
    }

    fun createStep(step: RPS) {

        val user = userControl
        user.steps.add(step)

        playerRoomManager.updateRoomPlayer(roomName, user, {

        }, {
            error.postValue(it)
        })
    }

    private fun savePlayerControl(room: Room) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                room.players.get(playerName)?.let {
                    userControl = it
                }
                roomData.postValue(room)
            }
        }
    }
}
package com.stenleone.rockpaperscissors.ui.fragments.findRoom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stenleone.rockpaperscissors.managers.network.ConnectRoomManager
import com.stenleone.rockpaperscissors.managers.network.ProfileCloudFirestoreManager
import com.stenleone.rockpaperscissors.model.network.Room
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConnectToRoomViewModel @Inject constructor(
    private val connectRoomManager: ConnectRoomManager,
    private val profileCloudFirestoreManager: ProfileCloudFirestoreManager
) : ViewModel() {

    val inProgress = MutableLiveData<Boolean>()
    val connected = MutableLiveData<Boolean>()
    val error = MutableLiveData<String?>()

    fun connect(room: Room, password: String) {
        inProgress.postValue(true)
        profileCloudFirestoreManager.getProfile(
            {
                connectRoomManager.connect(it, room, password, {
                    inProgress.postValue(false)
                    connected.postValue(true)
                }, {
                    inProgress.postValue(false)
                    error.postValue(it)
                }, {
                    inProgress.postValue(false)
                    error.postValue("")
                })
            }, {
                inProgress.postValue(false)
                error.postValue(it)
            }
        )
    }

    fun connect(room: Room) {
        inProgress.postValue(true)
        profileCloudFirestoreManager.getProfile(
            {
                connectRoomManager.connect(it, room, {
                    inProgress.postValue(false)
                    connected.postValue(true)
                }, {
                    inProgress.postValue(false)
                    error.postValue(it)
                }, {
                    inProgress.postValue(false)
                    error.postValue("")
                })
            }, {
                inProgress.postValue(false)
                error.postValue(it)
            }
        )
    }

}
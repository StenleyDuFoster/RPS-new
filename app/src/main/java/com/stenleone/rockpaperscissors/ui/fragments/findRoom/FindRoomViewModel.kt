package com.stenleone.rockpaperscissors.ui.fragments.findRoom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stenleone.rockpaperscissors.managers.FindRoomManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FindRoomViewModel @Inject constructor(
    val findRoomManager: FindRoomManager
) : ViewModel() {

    val listRooms = MutableLiveData<FindRoomManager.ListRooms>()

    init {
        observe()
    }

    fun observe() {
        findRoomManager.observeRooms(
            {
                listRooms.postValue(it)
            }, {

            }
        )
    }

}
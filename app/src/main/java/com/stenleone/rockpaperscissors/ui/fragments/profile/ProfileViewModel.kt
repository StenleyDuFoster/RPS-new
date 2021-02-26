package com.stenleone.rockpaperscissors.ui.fragments.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stenleone.rockpaperscissors.managers.ProfileCloudFirestoreManager
import com.stenleone.rockpaperscissors.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileCloudFirestoreManager: ProfileCloudFirestoreManager
) : ViewModel() {

    val profile = MutableLiveData<User>()
    val error = MutableLiveData<String>()

    init {
        getProfile()
    }

    fun getProfile() {
        profileCloudFirestoreManager.getProfile({
            profile.postValue(it)
        }, {

        })
    }
}
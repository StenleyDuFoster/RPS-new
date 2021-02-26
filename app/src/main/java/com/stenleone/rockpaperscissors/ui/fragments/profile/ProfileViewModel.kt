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

    val inProgress = MutableLiveData<Boolean>()

    val name = MutableLiveData<String>()
    val updateSuccess = MutableLiveData<Boolean?>()

    init {
        getProfile()
    }

    fun getProfile() {
        inProgress.postValue(true)
        profileCloudFirestoreManager.getProfile({
            profile.postValue(it)
            name.postValue(it.name)
            inProgress.postValue(false)
        }, {
            error.postValue(it)
            inProgress.postValue(false)
        })
    }

    fun updateName() {
        profile.value?.let { user ->
            name?.value?.let { name ->
                inProgress.postValue(true)
                val newUser = user
                user.name = name
                profileCloudFirestoreManager.updateUser(newUser, {
                    updateSuccess.postValue(true)
                    inProgress.postValue(false)
                }, {
                    error.postValue(it.message)
                    inProgress.postValue(false)
                })
            }
        }

    }
}
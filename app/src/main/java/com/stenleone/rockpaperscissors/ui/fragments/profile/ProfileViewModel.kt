package com.stenleone.rockpaperscissors.ui.fragments.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stenleone.rockpaperscissors.managers.general.ConnectionManager
import com.stenleone.rockpaperscissors.managers.network.fireStore.ProfileCloudFirestoreManager
import com.stenleone.rockpaperscissors.model.general.DataState
import com.stenleone.rockpaperscissors.model.network.User
import com.stenleone.stanleysfilm.model.entity.RequestError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileCloudFirestoreManager: ProfileCloudFirestoreManager,
    private val connectionManager: ConnectionManager
) : ViewModel() {

    val profile = MutableLiveData<User>()
    val error = MutableLiveData<RequestError>()


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
            if (connectionManager.isConnected.value == true) {
                error.postValue(RequestError(RequestError.REQUEST_ERROR, message = it, retryAction = { getProfile() }))
            } else {
                error.postValue(RequestError(RequestError.CONNECTION_ERROR, message = it, retryAction = { getProfile() }))
            }

            inProgress.postValue(false)
        })
    }

    fun updateName() {
        profile.value?.let { user ->
            name?.value?.let { name ->
                inProgress.postValue(true)
                val newUser = user
                user.name = name

                viewModelScope.launch {
                    val userUpdate = profileCloudFirestoreManager.updateUser(newUser)

                    when (userUpdate) {
                        is DataState.Success -> {
                            updateSuccess.postValue(true)
                            inProgress.postValue(false)
                        }
                        is DataState.Error -> {
                            if (connectionManager.isConnected.value == true) {
                                error.postValue(RequestError(RequestError.REQUEST_ERROR, message = userUpdate.exception.message, retryAction = { updateName() }))
                            } else {
                                error.postValue(RequestError(RequestError.CONNECTION_ERROR, message = userUpdate.exception.message, retryAction = { updateName() }))
                            }

                            inProgress.postValue(false)
                        }
                    }
                }
            }
        }

    }
}
package com.stenleone.rockpaperscissors.ui.fragments.signInOrRegister

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stenleone.rockpaperscissors.managers.general.ConnectionManager
import com.stenleone.rockpaperscissors.managers.network.ProfileCloudFirestoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInOrRegisterViewModel @Inject constructor(
    private val profileCloudFirestoreManager: ProfileCloudFirestoreManager,
    private val connectionManager: ConnectionManager
) : ViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val emailError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()
    val connectionError = MutableLiveData<() -> Unit>()

    val registerResult = MutableLiveData<Boolean>()
    val inProgress = MutableLiveData<Boolean>()

    fun register() {
        inProgress.postValue(true)
        profileCloudFirestoreManager.signInOrLogin(email.value ?: "", password.value ?: "", {
            registerResult.postValue(true)
        }, {
            if (connectionManager.isConnected.value == true) {
                emailError.postValue(it.localizedMessage)
            } else {
                connectionError.postValue({ register() })
            }

            inProgress.postValue(false)
        }, {
            if (connectionManager.isConnected.value == true) {
                passwordError.postValue(it.localizedMessage)
            } else {
                connectionError.postValue({ register() })
            }

            inProgress.postValue(false)
        })
    }
}
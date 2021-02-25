package com.stenleone.rockpaperscissors.ui.fragments.signInOrRegister

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stenleone.rockpaperscissors.managers.ProfileCloudFirestoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInOrRegisterViewModel @Inject constructor(
private val profileCloudFirestoreManager: ProfileCloudFirestoreManager
): ViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val registerResult = MutableLiveData<Boolean>()
    val inProgress = MutableLiveData<Boolean>()

    fun register() {
        inProgress.postValue(true)
        profileCloudFirestoreManager.signInOrLogin(email.value ?: "", password.value ?: "", {
            registerResult.postValue(true)
            inProgress.postValue(false)
        }, {

            inProgress.postValue(false)
        })
    }
}
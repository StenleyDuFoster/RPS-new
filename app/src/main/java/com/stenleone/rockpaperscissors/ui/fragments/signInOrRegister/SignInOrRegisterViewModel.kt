package com.stenleone.rockpaperscissors.ui.fragments.signInOrRegister

import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import com.stenleone.rockpaperscissors.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInOrRegisterViewModel @Inject constructor(

): BaseViewModel() {

    val name = MutableLiveData<EditText>()

    fun register() {

    }
}
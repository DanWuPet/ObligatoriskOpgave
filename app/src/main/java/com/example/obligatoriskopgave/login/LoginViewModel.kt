package com.example.obligatoriskopgave.login

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class LoginViewModel : ViewModel() {
    val userEmail: MutableLiveData<String> = MutableLiveData()

}
package com.gregorchristiaens.introduction.login

import android.util.Log
import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _navigateToProfile = MutableLiveData<Boolean>()
    val navigateToProfile: LiveData<Boolean>
        get() = _navigateToProfile

    init {
        _navigateToProfile.value = false
    }

    fun login() {
        Log.d("LoginViewModel", "Starting Login Email process")
        val email = email.value
        val password = password.value
        if (email != null && password != null) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("LoginTest", "signInWithEmailAndPassword:success")
                        _navigateToProfile.value = true
                    } else {
                        val message = task.exception!!.message.toString()
                        Log.d("LoginViewModel", message)
                    }
                }
        }
    }
}
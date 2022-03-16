package com.gregorchristiaens.introduction.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.gregorchristiaens.introduction.repository.UserRepository

class ProfileViewModel : ViewModel() {

    private val logKey = "IntroductionApp.KEY.ProfileViewModel"

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = UserRepository.getInstance()
    val user = database.user

    private val _navigateToLanding = MutableLiveData<Boolean>()
    val navigateToLanding: LiveData<Boolean>
        get() = _navigateToLanding

    init {
        _navigateToLanding.value = false
    }

    fun logout() {
        Log.d(logKey, "Logging out user")
        auth.signOut()
        val userRepository = UserRepository.getInstance()
        userRepository.resetUser()
        _navigateToLanding.value = true
    }
}
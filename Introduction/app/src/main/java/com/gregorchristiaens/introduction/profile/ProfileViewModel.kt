package com.gregorchristiaens.introduction.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.gregorchristiaens.introduction.domain.User
import com.gregorchristiaens.introduction.repository.UserRepository

class ProfileViewModel : ViewModel() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _navigateToLanding = MutableLiveData<Boolean>()
    val navigateToLanding: LiveData<Boolean>
        get() = _navigateToLanding

    init {
        _navigateToLanding.value = false
        val userRepository = UserRepository.getInstance()
        _user.value = userRepository.user.value
    }

    fun logout() {
        auth.signOut()
        _navigateToLanding.value = true
    }
}
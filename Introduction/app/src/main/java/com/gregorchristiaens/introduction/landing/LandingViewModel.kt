package com.gregorchristiaens.introduction.landing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.gregorchristiaens.introduction.domain.User
import com.gregorchristiaens.introduction.repository.UserRepository

class LandingViewModel(userRepository: UserRepository) : ViewModel() {
    /**
     * An instance of FirebaseAuth is acquired using [FirebaseAuth.getInstance]
     * This ensures ensures we use a single instance of [FirebaseAuth] throughout the application.
     */
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var _firebaseUser = MutableLiveData<FirebaseUser>()
    val firebaseUser: LiveData<FirebaseUser>
        get() = _firebaseUser

    private var _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user


    init {
        val fu = auth.currentUser
        if (fu != null) {
            _user.value = userRepository.getUserData(fu)
            _firebaseUser.value = fu!!
        }
    }
}
package com.gregorchristiaens.introduction.landing

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.gregorchristiaens.introduction.domain.User
import com.gregorchristiaens.introduction.repository.UserRepository

class LandingViewModel(userRepository: UserRepository) : ViewModel() {
    private val logKey = "IntroductionApp.KEY.LandingViewModel"

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
        val fu: FirebaseUser
        if (auth.currentUser != null) {
            fu = auth.currentUser!!
            Log.d(logKey, "Found a logged in user: ${fu.uid}")
            //When the user returns his info is stored and acquired in the UserRepository INSTANCE
            userRepository.getUser(fu.uid)
            _user.value = userRepository.user.value
            _firebaseUser.value = fu
        }
    }
}
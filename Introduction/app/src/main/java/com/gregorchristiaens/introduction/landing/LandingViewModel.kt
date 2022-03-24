package com.gregorchristiaens.introduction.landing

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LandingViewModel : ViewModel() {

    private val logKey = "IntroductionApp.LOGKEY.LandingViewModel"

    /**
     * An instance of FirebaseAuth is acquired using [FirebaseAuth.getInstance]
     * This ensures ensures we use a single instance of [FirebaseAuth] throughout the application.
     */
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var _user = MutableLiveData<FirebaseUser>()
    val user: LiveData<FirebaseUser>
        get() = _user

    init {
        val fu: FirebaseUser
        if (auth.currentUser != null) {
            fu = auth.currentUser!!
            Log.d("$logKey.init", "Found a logged in user: ${fu.uid}")
            _user.value = fu
        }
    }
}
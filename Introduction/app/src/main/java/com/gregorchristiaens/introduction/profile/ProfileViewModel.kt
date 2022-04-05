package com.gregorchristiaens.introduction.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.gregorchristiaens.introduction.repository.UserRepository

class ProfileViewModel : ViewModel() {

    private val logKey = "IntroductionApp.LOGKEY.ProfileViewModel"

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = UserRepository.getInstance()
    val user = database.user

    fun leaveKarateClub() {
        val u = user.value
        if (u != null) {
            u.karateClubId = null
            database.addUser(u)
        }
    }
}
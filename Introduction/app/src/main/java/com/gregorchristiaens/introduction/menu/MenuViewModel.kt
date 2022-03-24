package com.gregorchristiaens.introduction.menu

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.gregorchristiaens.introduction.repository.LessonRepository
import com.gregorchristiaens.introduction.repository.UserRepository

class MenuViewModel : ViewModel() {

    private val logKey = "IntroductionApp.LOGKEY.MenuViewModel"

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userRepository = UserRepository.getInstance()
    private val lessonRepository = LessonRepository.getInstance()

    private val _navigateToLanding = MutableLiveData(false)
    val navigateToLanding: LiveData<Boolean>
        get() = _navigateToLanding

    init {
        lessonRepository.getLessons()
        auth.uid?.let { userRepository.getUser(it) }
    }

    fun logout() {
        Log.d("$logKey.logout", "Logging out user")
        auth.signOut()
        userRepository.resetUser()
        _navigateToLanding.value = true
    }
}
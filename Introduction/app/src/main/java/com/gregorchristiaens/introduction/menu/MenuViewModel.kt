package com.gregorchristiaens.introduction.menu


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.gregorchristiaens.introduction.domain.KarateClub
import com.gregorchristiaens.introduction.repository.KarateClubRepository
import com.gregorchristiaens.introduction.repository.LessonRepository
import com.gregorchristiaens.introduction.repository.UserRepository

class MenuViewModel : ViewModel() {

    private val logKey = "IntroductionApp.LOGKEY.MenuViewModel"

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val userRepository = UserRepository.getInstance()
    private val lessonRepository = LessonRepository.getInstance()
    private val karateClubRepository = KarateClubRepository.getInstance()

    private val _navigateToLanding = MutableLiveData(false)
    val navigateToLanding: LiveData<Boolean>
        get() = _navigateToLanding

    private val _hasKarateClub = MutableLiveData(false)
    val hasKarateClub: LiveData<Boolean>
        get() = _hasKarateClub

    init {
        //Profile Preload in background
        auth.uid?.let { userRepository.getUser(it) }
        //Karate Preload in background
        karateClubRepository.getClubList()
        userRepository.user.value?.let {
            if (!it.karateClubId.isNullOrEmpty()) {
                _hasKarateClub.value = true
                Log.d(logKey, "KarateClubId = ${it.karateClubId}")
                karateClubRepository.getKarateClub(it.karateClubId!!)
                lessonRepository.karateClubId = it.karateClubId!!
                lessonRepository.getLessons()
            } else {
                //karateClubRepository.getClubList()
                _hasKarateClub.value = false
            }
        }

        /*
        karateClubRepository.addKarateClub(
            KarateClub(
                "chie_on_chikara",
                "y2PRj7QSirVRDaPD5vGYxJKALVB3"
            )
        )
        lessonRepository.karateClubId = "chie_on_chikara"
        lessonRepository.addDefaultLessons()
         */
    }

    fun logout() {
        Log.d("$logKey.logout", "Logging out user")
        auth.signOut()
        userRepository.resetUser()
        _navigateToLanding.value = true
    }

    fun checkKarateClub(): Boolean {
        userRepository.user.value?.let {
            if (!it.karateClubId.isNullOrEmpty()) {
                Log.d(logKey, "KarateClubId = ${it.karateClubId}")
                lessonRepository.karateClubId = it.karateClubId!!
                _hasKarateClub.value = true
                karateClubRepository.getKarateClub(it.karateClubId!!)
                lessonRepository.getLessons()
                return true

            } else {
                //karateClubRepository.getClubList()
                _hasKarateClub.value = false
                return false
            }
        }
        return false
    }

}
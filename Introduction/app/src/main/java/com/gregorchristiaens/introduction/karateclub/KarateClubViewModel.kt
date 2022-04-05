package com.gregorchristiaens.introduction.karateclub

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gregorchristiaens.introduction.repository.KarateClubRepository
import com.gregorchristiaens.introduction.repository.LessonRepository
import com.gregorchristiaens.introduction.repository.UserRepository

class KarateClubViewModel : ViewModel() {

    private val logKey = "IntroductionApp.LOGKEY.KarateClubViewModel"

    private val userRepository = UserRepository.getInstance()
    private val karateClubRepository = KarateClubRepository.getInstance()
    private val lessonRepository = LessonRepository.getInstance()

    val karateClubCode = MutableLiveData<String>()

    private val _navigateToLessons = MutableLiveData(false)
    val navigateToLessons: LiveData<Boolean>
        get() = _navigateToLessons

    private val imageAnalyzer: ImageAnalyzer = ImageAnalyzer()

    fun joinKarateClub() {
        val user = userRepository.user.value
        if (user != null) {
            if (user.karateClubId == null || user.karateClubId!!.trim() == "" && karateClubCode.value != null && karateClubCode.value.toString()
                    .trim() != ""
            ) {
                val karateClubId = karateClubCode.value!!
                val clubs = karateClubRepository.clubs.value
                if (clubs != null && clubs.map { club -> club.id }.contains(karateClubId)) {
                    karateClubRepository.getKarateClub(karateClubId)
                    user.karateClubId = karateClubId
                    userRepository.addUser(user)
                    lessonRepository.karateClubId = karateClubId
                    lessonRepository.getLessons()
                    _navigateToLessons.value = true
                } else {
                    Log.d(logKey, "There is no club with the entered indentification")
                }
            } else {
                Log.d(logKey, "Please enter a valid club identification")
                _navigateToLessons.value = false
            }
        }
    }

    fun leaveKarateClub() {
        //TODO implement leave and leave button
    }

    fun scanQrCode() {}
}
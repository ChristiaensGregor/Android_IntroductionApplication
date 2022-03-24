package com.gregorchristiaens.introduction.lesson

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gregorchristiaens.introduction.domain.Lesson
import com.gregorchristiaens.introduction.domain.User
import com.gregorchristiaens.introduction.repository.LessonRepository
import com.gregorchristiaens.introduction.repository.UserRepository

class LessonViewModel : ViewModel() {

    private val logKey = "IntroductionApp.LOGKEY.LessonViewModel"

    private val lessonRepository = LessonRepository.getInstance()
    private val userRepository = UserRepository.getInstance()

    val lesson: LiveData<Lesson> = lessonRepository.lesson

    private val user: LiveData<User> = userRepository.user

    private val _participateOn = MutableLiveData(true)
    val participateOn: LiveData<Boolean> get() = _participateOn

    init {
        val l = lesson.value
        val u = user.value
        if (l != null && u != null) {
            if (!l.users.isNullOrEmpty()) {
                _participateOn.value = !l.users.contains(u.id)
            }
        }
    }

    fun participate() {
        val l = lesson.value
        val u = user.value
        if (u != null && l != null) {
            lessonRepository.participate(u.id)
            _participateOn.value = !l.users.contains(u.id)
        }
    }

    fun unParticipate() {
        val l = lesson.value
        val u = user.value
        if (u != null && l != null) {
            lessonRepository.unParticipate(u.id)
            _participateOn.value = !l.users.contains(u.id)
        }
    }
}
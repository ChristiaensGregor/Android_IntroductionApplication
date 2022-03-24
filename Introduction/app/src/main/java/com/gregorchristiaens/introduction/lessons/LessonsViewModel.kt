package com.gregorchristiaens.introduction.lessons

import androidx.lifecycle.ViewModel
import com.gregorchristiaens.introduction.repository.LessonRepository

class LessonsViewModel : ViewModel() {

    private val logKey = "IntroductionApp.LOGKEY.LessonsViewModel"

    private val lessonRepository = LessonRepository.getInstance()
    val lessons = lessonRepository.lessons

    init {
        //lessonRepository.addDefaultLessons()
    }
}
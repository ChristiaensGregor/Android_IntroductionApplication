package com.gregorchristiaens.introduction

import com.gregorchristiaens.introduction.domain.DatabaseLesson
import com.gregorchristiaens.introduction.domain.Lesson
import com.gregorchristiaens.introduction.domain.LessonTypes
import org.junit.Assert
import org.junit.Test

class DatabaseLessonUnitTest {

    private val dbl1 = DatabaseLesson(
        "TestLesson1", "Wingene", "StandardIppan", "01/01/2022",
        arrayListOf()
    )
    private val l1 = Lesson(
        "TestLesson1", "Wingene", LessonTypes.StandardIppan, "01/01/2022",
        arrayListOf()
    )


    @Test
    fun convertDatabaseLessonToLesson() {
        val l = dbl1.convertToLesson()
        Assert.assertEquals(l.id, l1.id)
        Assert.assertEquals(l.name, l1.name)
        Assert.assertEquals(l.location, l1.location)
        Assert.assertEquals(l.type, l1.type)
        Assert.assertEquals(l.dateString, l1.dateString)
        Assert.assertEquals(l.date, l1.date)
        Assert.assertEquals(l.users, l1.users)
    }
}
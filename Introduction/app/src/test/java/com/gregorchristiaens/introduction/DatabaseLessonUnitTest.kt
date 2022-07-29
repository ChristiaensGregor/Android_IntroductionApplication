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
    private val dbl2 = DatabaseLesson(
        "TestLesson1", "Wingene", "StandardIppan", "01/01/2022",
        arrayListOf("user1", "user2")
    )
    private val l1 = Lesson(
        "TestLesson1", "Wingene", LessonTypes.StandardIppan, "01/01/2022",
        arrayListOf()
    )
    private val l2 = Lesson(
        "TestLesson1", "Wingene", LessonTypes.StandardIppan, "01/01/2022",
        arrayListOf("user1", "user2")
    )

    @Test
    fun constructDatabaseLesson_NoParameters() {
        val l = DatabaseLesson()
        Assert.assertNotNull(l)
    }

    @Test
    fun constructDatabaseLesson_Parameters_NoUsers() {
        val l = DatabaseLesson(
            "TestLesson1",
            "Wingene",
            "StandardIppan",
            "01/01/2022",
            arrayListOf()
        )
        Assert.assertNotNull(l)
        Assert.assertEquals(l.id, "TestLesson1")
        Assert.assertEquals(l.location, "Wingene")
        Assert.assertEquals(l.type, "StandardIppan")
        Assert.assertEquals(l.date, "01/01/2022")
        Assert.assertTrue(l.users!!.size == 0)
    }

    @Test
    fun constructDatabaseLesson_Parameters_Users() {
        val l = DatabaseLesson(
            "TestLesson1",
            "Wingene",
            "StandardIppan",
            "01/01/2022",
            arrayListOf("user1", "user2")
        )
        Assert.assertNotNull(l)
        Assert.assertEquals(l.id, "TestLesson1")
        Assert.assertEquals(l.location, "Wingene")
        Assert.assertEquals(l.type, "StandardIppan")
        Assert.assertEquals(l.date, "01/01/2022")
        Assert.assertTrue(l.users!!.size == 2)
    }

    @Test(expected = IllegalArgumentException::class)
    fun convertDatabaseLessonToLesson_NoParameters() {
        val l = DatabaseLesson().convertToLesson()
        Assert.assertEquals(l.javaClass, Lesson::class.java)
    }


    @Test
    fun convertDatabaseLessonToLesson_CheckClass_NoUsers() {
        val l = dbl1.convertToLesson()
        Assert.assertEquals(l.javaClass, Lesson::class.java)
    }

    @Test
    fun convertDatabaseLessonToLesson_CheckValues_NoUsers() {
        val l = dbl1.convertToLesson()
        Assert.assertEquals(l.id, l1.id)
        Assert.assertEquals(l.name, l1.name)
        Assert.assertEquals(l.location, l1.location)
        Assert.assertEquals(l.type, l1.type)
        Assert.assertEquals(l.dateString, l1.dateString)
        Assert.assertEquals(l.date, l1.date)
        Assert.assertEquals(l.users, l1.users)
    }

    @Test
    fun convertDatabaseLessonToLesson_CheckClass_Users() {
        val l = dbl2.convertToLesson()
        Assert.assertEquals(l.javaClass, Lesson::class.java)
    }

    @Test
    fun convertDatabaseLessonToLesson_CheckValues_Users() {
        val l = dbl2.convertToLesson()
        Assert.assertEquals(l.users, l2.users)
    }

    @Test(expected = IllegalArgumentException::class)
    fun convertDatabaseLessonToLesson_ParameterType_InvalidType() {
        val l = DatabaseLesson(
            "id",
            "location",
            "invalid_type",
            "date",
            arrayListOf()
        ).convertToLesson()
    }
}
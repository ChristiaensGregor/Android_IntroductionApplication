package com.gregorchristiaens.introduction.domain

import java.lang.IllegalArgumentException

@Suppress("JoinDeclarationAndAssignment")
class DatabaseLesson() {

    lateinit var id: String
    lateinit var location: String
    lateinit var type: String
    lateinit var date: String
    var users: ArrayList<String>? = arrayListOf()

    constructor(
        _id: String,
        _location: String,
        _type: String,
        _date: String,
        _users: ArrayList<String>
    ) : this() {
        id = _id
        location = _location
        type = _type
        date = _date
        users = _users
    }

    /**
     * @throws IllegalArgumentException
     */
    fun convertToLesson(): Lesson {
        if (this::id.isInitialized) {
            return if (users.isNullOrEmpty()) {
                Lesson(id, location, getLessonType(type), date, arrayListOf())
            } else {
                Lesson(id, location, getLessonType(type), date, users!!)
            }
        } else {
            throw IllegalArgumentException("Could not convert DatabaseLesson due to missing values.")
        }
    }

    /**
     *@throws IllegalArgumentException
     */
    private fun getLessonType(typeString: String): LessonTypes {
        for (type in LessonTypes.values()) {
            if (type.name == typeString) return type
        }
        throw IllegalArgumentException("Could not find a matching LessonType for the string $typeString")
    }
}
package com.gregorchristiaens.introduction.domain

import android.os.Build
import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class Lesson() : Serializable {
    var id: String = ""
    val name: String
        get() = "$location ${type.name}"
    var location: String = ""
    var type: LessonTypes = LessonTypes.StandardIppan
    var dateString: String = ""
    var date: LocalDate? = null
        get() {
            return if (field == null) {
                convertDate(dateString)
            } else {
                field
            }
        }
    var users: ArrayList<String> = arrayListOf()

    constructor(
        _id: String,
        _location: String,
        _type: LessonTypes,
        _date: String,
        _users: ArrayList<String>
    ) : this() {
        id = _id
        location = _location
        type = _type
        dateString = _date
        users = _users
    }

    private fun convertDate(date: String): LocalDate? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.parse(
                date,
                DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRANCE)
            )
        } else {
            null
        }
    }
}
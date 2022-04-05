package com.gregorchristiaens.introduction.domain

@Suppress("JoinDeclarationAndAssignment")
class DatabaseLesson() {

    lateinit var id: String
    lateinit var location: String
    lateinit var type: String
    lateinit var date: String
    var users: ArrayList<String> = arrayListOf()

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

    fun convertToLesson(): Lesson {
        var t: LessonTypes = LessonTypes.StandardIppan
        when (type) {
            LessonTypes.StandardIppan.name -> t = LessonTypes.StandardIppan
            LessonTypes.KobudoBo.name -> t = LessonTypes.KobudoBo
            LessonTypes.KobudoNunchaku.name -> t = LessonTypes.KobudoNunchaku
            LessonTypes.KobudoSai.name -> t = LessonTypes.KobudoSai
            LessonTypes.KobudoTonfa.name -> t = LessonTypes.KobudoTonfa
            LessonTypes.Kumite.name -> t = LessonTypes.Kumite
        }
        return Lesson(id, location, t, date, users)
    }
}
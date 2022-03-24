package com.gregorchristiaens.introduction.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gregorchristiaens.introduction.domain.DatabaseLesson
import com.gregorchristiaens.introduction.domain.Lesson
import com.gregorchristiaens.introduction.domain.LessonTypes
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import java.lang.reflect.InvocationTargetException
import kotlin.collections.ArrayList

class LessonRepository {

    private val logKey = "IntroductionApp.LOGKEY.LessonRepository"

    private var database: DatabaseReference =
        Firebase.database("https://introduction-17d67-default-rtdb.europe-west1.firebasedatabase.app").reference.child(
            "lessons"
        )

    private var _lessons = MutableLiveData<List<Lesson>>()
    val lessons: LiveData<List<Lesson>>
        get() = _lessons

    private var _lesson = MutableLiveData<Lesson>()
    val lesson: LiveData<Lesson> get() = _lesson

    /**
     * addLesson
     * Add a Lesson to the realtime database in Firebase.
     * Converts the lesson to a DatabaseLesson first.
     * This way we only store the values we really need and the database can handle easily.
     */
    private fun addLesson(lesson: Lesson) {
        try {
            database.child(lesson.id).setValue(convertToDatabaseLesson(lesson))
                .addOnCompleteListener {
                    Log.d("$logKey.addLesson", "Saved Lesson: ${lesson.name}")
                }
                .addOnFailureListener {
                    Log.d("$logKey.addLesson", "Failed to save the users data in firebase database")
                }
        } catch (err: InvocationTargetException) {
            err.message?.let { Log.d(logKey, it) }
        }

    }

    /**
     * getLessonsOnce
     * This version of getLessons requests the lessons once and does not update it afterwards.
     *
     * @throws IllegalArgumentException if the conversion to [DatabaseLesson] fails.
     */
    fun getLessonsOnce() {
        database.get().addOnSuccessListener {
            Log.d("$logKey.getLessons", "Got value ${it.value}")
            try {
                val list = ArrayList<Lesson>()
                for (lesson in it.children) {
                    val l = lesson.getValue(DatabaseLesson::class.java)
                    if (l == null) {
                        throw IllegalArgumentException("Could not convert the database object to the local Lesson class")
                    } else {
                        list.add(convertToLocalLesson(l))
                    }
                }
                _lessons.value = list
                sortLessons()
            } catch (ex: InvocationTargetException) {
                ex.message?.let { message -> Log.d(logKey, message) }
            } catch (ex: IllegalArgumentException) {
                ex.message?.let { message -> Log.d(logKey, message) }
            } catch (ex: RuntimeException) {
                ex.message?.let { message -> Log.d(logKey, message) }
            }
        }.addOnFailureListener {
            Log.d("$logKey.getLessons", "Error getting data", it)
        }
    }

    /**
     * getLessons
     * Requests lessons from [database] (child:"lessons").
     * Listens to onDataChange to update the lessons once a change occurs in the realtime database.
     * Converts the gotten data to [DatabaseLesson] first.
     * The converted DatabaseLessons are then converted with [convertToLocalLesson] to [Lesson] for use in the application.
     * Uses [sortLessons] to sort the acquired lessons.
     *
     * @throws IllegalArgumentException if the conversion to [DatabaseLesson] fails.
     */
    fun getLessons() {
        database.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NullSafeMutableLiveData")
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("$logKey.getLessons", "Getting lessons from Database")
                Log.d("$logKey.getLessons", "Got Lessons: ${snapshot.value}")
                val list = ArrayList<Lesson>()
                for (lesson in snapshot.children) {
                    val l = lesson.getValue(DatabaseLesson::class.java)
                    if (l == null) {
                        throw IllegalArgumentException("Could not convert the database object to the local Lesson class")
                    } else {
                        list.add(convertToLocalLesson(l))
                    }
                }
                _lessons.value = list
                sortLessons()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(logKey, "Failed to read value.", error.toException())
            }
        })
    }

    /**
     * Sort the lessons based on the date of occurrence.
     * This method can be called after each update.
     */
    @SuppressLint("NullSafeMutableLiveData")
    fun sortLessons() {
        var list = _lessons.value
        if (!list.isNullOrEmpty()) {
            list = list.sortedBy { it.date }
            _lessons.value = list
        }
    }

    /**
     * Set the value of [lesson] used to store the currently inspected lesson.
     * This lesson is used when adding users and in the viewmodel of the lesson detail view.
     */
    fun setCurrentLesson(lesson: Lesson) {
        _lesson.value = lesson
    }

    /**
     * Add the current user to the currently inspected value of [lesson].
     * If the user is already participating we should end this flow quickly.
     */
    @SuppressLint("NullSafeMutableLiveData")
    fun participate(id: String) {
        val l = _lesson.value
        if (l != null) {
            if (!l.users.contains(id)) {
                l.users.add(id)
                _lesson.value = l
                addLesson(l)
            }
        }
    }

    /**
     *
     */
    @SuppressLint("NullSafeMutableLiveData")
    fun unParticipate(id: String) {
        val l = _lesson.value
        if (l != null) {
            if (l.users.contains(id)) {
                l.users.remove(id)
                _lesson.value = l
                addLesson(l)
            }
        }
    }

    private fun convertToDatabaseLesson(lesson: Lesson): DatabaseLesson {
        return DatabaseLesson(
            lesson.id,
            lesson.location,
            lesson.type.toString(),
            lesson.dateString,
            lesson.users
        )
    }

    private fun convertToLocalLesson(lesson: DatabaseLesson): Lesson {
        var type: LessonTypes = LessonTypes.StandardIppan
        when (lesson.type) {
            LessonTypes.StandardIppan.name -> type = LessonTypes.StandardIppan
            LessonTypes.KobudoBo.name -> type = LessonTypes.KobudoBo
            LessonTypes.KobudoNunchaku.name -> type = LessonTypes.KobudoNunchaku
            LessonTypes.KobudoSai.name -> type = LessonTypes.KobudoSai
            LessonTypes.KobudoTonfa.name -> type = LessonTypes.KobudoTonfa
            LessonTypes.Kumite.name -> type = LessonTypes.Kumite
        }
        return Lesson(lesson.id, lesson.location, type, lesson.date, lesson.users)
    }

    @Suppress("unused")
    fun addDefaultLessons() {
        val lesson1 = Lesson(
            "LW18032022",
            "Wingene",
            LessonTypes.StandardIppan,
            "18/03/2022",
            arrayListOf("SNdJLrijB2MjHpW5MHn8WuSymU33")
        )
        val lesson2 = Lesson(
            "LA22032022",
            "Maria-Aalter",
            LessonTypes.KobudoSai,
            "22/03/2022",
            arrayListOf("SNdJLrijB2MjHpW5MHn8WuSymU33")
        )
        val lesson3 = Lesson(
            "LW25032022",
            "Wingene",
            LessonTypes.KobudoBo,
            "25/03/2022",
            arrayListOf()
        )
        val lesson4 = Lesson(
            "LA29032022",
            "Maria-Aalter",
            LessonTypes.KobudoTonfa,
            "29/03/2022",
            arrayListOf()
        )
        val lesson5 = Lesson(
            "LW01042022",
            "Wingene",
            LessonTypes.KobudoNunchaku,
            "01/04/2022",
            arrayListOf()
        )
        val lesson6 = Lesson(
            "LA05042022",
            "Maria-Aalter",
            LessonTypes.Kumite,
            "05/04/2022",
            arrayListOf()
        )
        val lesson7 = Lesson(
            "LW08042022",
            "Wingene",
            LessonTypes.StandardIppan,
            "08/04/2022",
            arrayListOf()
        )
        val lesson8 = Lesson(
            "LA12042022",
            "Maria-Aalter",
            LessonTypes.StandardIppan,
            "12/04/2022",
            arrayListOf()
        )
        val lesson9 = Lesson(
            "LW15042022",
            "Wingene",
            LessonTypes.StandardIppan,
            "15/04/2022",
            arrayListOf()
        )
        addLesson(lesson1)
        addLesson(lesson2)
        addLesson(lesson3)
        addLesson(lesson4)
        addLesson(lesson5)
        addLesson(lesson6)
        addLesson(lesson7)
        addLesson(lesson8)
        addLesson(lesson9)
    }

    companion object {

        @Volatile
        private var INSTANCE: LessonRepository? = null

        fun getInstance(): LessonRepository {
            var instance: LessonRepository? = INSTANCE
            if (instance == null) {
                instance = LessonRepository()
                INSTANCE = instance
            }
            return instance
        }
    }
}
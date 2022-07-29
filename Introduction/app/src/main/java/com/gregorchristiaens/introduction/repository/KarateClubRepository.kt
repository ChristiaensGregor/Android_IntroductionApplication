package com.gregorchristiaens.introduction.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gregorchristiaens.introduction.domain.KarateClub
import java.lang.IllegalArgumentException
import java.lang.reflect.InvocationTargetException

class KarateClubRepository : Repository() {

    private val logKey = "IntroductionApp.LOGKEY.KarateClubRepository"

    override val childPaths: ArrayList<String> = arrayListOf("clubs")

    private var _club = MutableLiveData<KarateClub>()
    val club: LiveData<KarateClub> get() = _club

    private var _clubList = MutableLiveData<ArrayList<String>>()
    val clubList: LiveData<ArrayList<String>> get() = _clubList

    /**
     * addKarateClub
     * Add a [KarateClub] to the realtime database in Firebase.
     * Uses [KarateClub.id] as a unique identifier.
     */
    fun addKarateClub(club: KarateClub) {
        try {
            database.child(club.id).setValue(club)
                .addOnCompleteListener {
                    Log.d("$logKey.addKarateClub", "Saved KarateClub: ${club.id}")
                }
                .addOnFailureListener {
                    Log.d(
                        "$logKey.addKarateClub",
                        "Failed to save the KarateClub in firebase database", it
                    )
                }
        } catch (err: InvocationTargetException) {
            err.message?.let { Log.d(logKey, it) }
        }
    }

    /**
     * getKarateClub
     * Get the [KarateClub] with the id [clubId] from the realtime database.
     *
     * @throws IllegalArgumentException if the conversion to [KarateClub] fails.
     */
    @SuppressLint("NullSafeMutableLiveData")
    fun getKarateClub(clubId: String) {
        Log.d("$logKey.getKarateClub", "Getting KarateClub from Database")
        database.child(clubId).get().addOnSuccessListener {
            Log.i("$logKey.getKarateClub", "Got KarateClub ${it.value}")
            try {
                val club = it.getValue(KarateClub::class.java)
                if (club == null) {
                    throw IllegalArgumentException("Could not convert the database object to the local KarateClub class")
                } else {
                    _club.value = club
                }
            } catch (ex: IllegalArgumentException) {
                ex.message?.let { it1 -> Log.d(logKey, it1) }
            }
        }.addOnFailureListener {
            Log.e("$logKey.getKarateClub", "Error getting data", it)
        }
    }

    fun getClubList() {
        Log.d("$logKey.getClubList", "Getting KarateClubs from Database")
        database.child("club_list").get().addOnSuccessListener {
            Log.i("$logKey.getClubList", "Got ClubList ${it.value}")
            val list = ArrayList<String>()
            for (clubObject in it.children) {
                val club = clubObject.getValue()
                if (club == null) {
                    throw IllegalArgumentException("Could not convert the database object to the local Club class")
                } else {
                    list.add(club.toString())
                }
            }
            _clubList.value = list
        }.addOnFailureListener {
            Log.e("$logKey.getClubList", "Error getting data", it)
        }
    }

    fun addClubList(clubList: ArrayList<String>) {
        try {
            database.child("club_list").setValue(clubList)
                .addOnCompleteListener {
                    Log.d("$logKey.addClubList", "Saved ClubList: $clubList")
                }
                .addOnFailureListener {
                    Log.d(
                        "$logKey.addClubList",
                        "Failed to save the KarateClub in firebase database", it
                    )
                }
        } catch (err: InvocationTargetException) {
            err.message?.let { Log.d(logKey, it) }
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: KarateClubRepository? = null

        fun getInstance(): KarateClubRepository {
            var instance: KarateClubRepository? = INSTANCE
            if (instance == null) {
                instance = KarateClubRepository()
                instance.getDatabaseInstance()
                INSTANCE = instance
            }
            return instance
        }
    }

}
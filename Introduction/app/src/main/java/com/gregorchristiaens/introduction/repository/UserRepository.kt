package com.gregorchristiaens.introduction.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gregorchristiaens.introduction.domain.User
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UserRepository : Repository() {

    private val logKey = "IntroductionApp.LOGKEY.UserRepository"

    override val childPaths: ArrayList<String> = arrayListOf("users")

    private var _user = MutableLiveData(User())
    val user: LiveData<User>
        get() = _user

    fun addUser(user: User) {
        val date = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.FRENCH).format(Date())
        user.joinDate = date
        database.child(user.id).setValue(user).addOnCompleteListener {
            Log.d("$logKey.addUser", "Saved users data in firebase database")
        }
            .addOnFailureListener {
                Log.d("$logKey.addUser", "Failed to save the users data in firebase database")
            }
        _user.value = user
    }

    /**
     * getUser
     * This version of getUser requests the user once and does not update it afterwards
     */
    @SuppressLint("NullSafeMutableLiveData")
    fun getUser(id: String) {
        _user.value?.id = id
        Log.d("$logKey.getUser", "Getting user from Database")
        database.child(id).get().addOnSuccessListener {
            Log.i("$logKey.getUser", "Got User ${it.value}")
            try {
                val user = it.getValue(User::class.java)
                if (user == null) {
                    throw IllegalArgumentException("Could not convert the database object to the local User class")
                } else {
                    _user.value = user
                }
            } catch (ex: IllegalArgumentException) {
                ex.message?.let { it1 -> Log.d(logKey, it1) }
            }
        }.addOnFailureListener {
            Log.e("$logKey.getUser", "Error getting data", it)
        }
    }

    /**
     * getUser
     * This version of getUser keeps the value of user up to date when it changes in the firebase database
     */
/*    fun getUser(id: String) {
        database.child(id)
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NullSafeMutableLiveData")
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(logKey, "Getting user from Database")
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        Log.d(
                            logKey,
                            "Got value ${user.displayName}, ${user.email}, ${user.joinDate}, ${user.id}"
                        )
                        _user.value = user
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(logKey, "Failed to read value.", error.toException())
                }
            })
    }*/

    fun resetUser() {
        _user.value = User()
    }

    companion object {

        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(): UserRepository {
            var instance: UserRepository? = INSTANCE
            if (instance == null) {
                instance = UserRepository()
                instance.getDatabaseInstance()
                INSTANCE = instance
            }
            return instance
        }
    }
}
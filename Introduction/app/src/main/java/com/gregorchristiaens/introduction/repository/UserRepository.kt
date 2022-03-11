package com.gregorchristiaens.introduction.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.gregorchristiaens.introduction.domain.User

class UserRepository {

    val logKey = "UserRepository"

    private var _user = MutableLiveData<User>(User())
    val user: LiveData<User>
        get() = _user


    fun setUserData(firebaseUser: FirebaseUser) {
        //TODO call database and get userData
        Log.d(logKey, "Setting the email and password values of user")
        firebaseUser.email?.let {
            _user.value?.email = it
        }
        firebaseUser.displayName?.let {
            _user.value?.displayName = it
        }
    }

    fun resetUser() {
        _user.value = User()
    }

    companion object {

        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(): UserRepository {
            var instance = INSTANCE
            if (instance == null) {
                instance = UserRepository()
                INSTANCE = instance
            }
            return instance
        }
    }
}
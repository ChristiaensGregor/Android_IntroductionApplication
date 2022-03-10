package com.gregorchristiaens.introduction.repository

import com.google.firebase.auth.FirebaseUser
import com.gregorchristiaens.introduction.domain.User

class UserRepository {
    private var user = User()


    fun getUserData(firebaseUser: FirebaseUser): User {
        //TODO call database and get userData
        firebaseUser.email?.let { user.email = it }
        firebaseUser.displayName?.let { user.displayName = it }
        return user
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
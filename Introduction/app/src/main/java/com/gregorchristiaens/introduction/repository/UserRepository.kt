package com.gregorchristiaens.introduction.repository

import com.google.firebase.auth.FirebaseUser
import com.gregorchristiaens.introduction.domain.User

class UserRepository {
    var users = mutableListOf<User>()

    fun addUser(email: String) {
        val user = User()
        user.setMail(email)
        users.add(user)
    }
}
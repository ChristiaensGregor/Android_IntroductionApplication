package com.gregorchristiaens.introduction.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gregorchristiaens.introduction.repository.UserRepository

class RegisterViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
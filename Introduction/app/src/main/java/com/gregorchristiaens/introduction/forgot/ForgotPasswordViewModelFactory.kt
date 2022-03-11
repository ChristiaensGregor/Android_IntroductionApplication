package com.gregorchristiaens.introduction.forgot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gregorchristiaens.introduction.repository.UserRepository
import java.lang.IllegalArgumentException

class ForgotPasswordViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java)) {
            return ForgotPasswordViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
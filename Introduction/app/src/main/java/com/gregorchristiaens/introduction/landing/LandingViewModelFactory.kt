package com.gregorchristiaens.introduction.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gregorchristiaens.introduction.repository.UserRepository

class LandingViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LandingViewModel::class.java)) {
            return LandingViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
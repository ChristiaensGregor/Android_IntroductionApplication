package com.gregorchristiaens.introduction.forgot

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.gregorchristiaens.introduction.repository.UserRepository

class ForgotPasswordViewModel : ViewModel() {

    private val logKey = "IntroductionApp.LOGKEY.ForgotPasswordViewModel"

    private var userRepository: UserRepository = UserRepository.getInstance()

    var email = MutableLiveData<String>()

    private val auth = FirebaseAuth.getInstance()


    private var _resetSuccess = MutableLiveData<Boolean>()
    val resetSuccess: LiveData<Boolean>
        get() = _resetSuccess

    private var _resetError = MutableLiveData<String>()
    val resetError: LiveData<String>
        get() = _resetError

    fun resetPassword() {
        val email = email.value
        if (validateEmail(email) && email != null) {
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("$logKey.resetPassword", "Reset Mail sent")
                    userRepository.user.value?.email = email
                    _resetSuccess.value = true
                } else {
                    _resetSuccess.value = false
                    _resetError.value = task.exception!!.message.toString()
                }
            }
        }
    }

    private var _emailError = MutableLiveData<String>()
    val emailError: LiveData<String>
        get() = _emailError

    private fun validateEmail(value: String?): Boolean {
        if (!value.isNullOrEmpty() && value.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(value)
                .matches()
        ) {
            _emailError.value = ""
            return true
        }
        _emailError.value = "Please enter a valid email address"
        return false
    }

}
package com.gregorchristiaens.introduction.register

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.gregorchristiaens.introduction.domain.User
import com.gregorchristiaens.introduction.repository.UserRepository

class RegisterViewModel : ViewModel() {

    private val logKey = "IntroductionApp.LOGKEY.RegisterViewModel"

    private var userRepository: UserRepository = UserRepository.getInstance()

    var displayName = MutableLiveData<String>()

    var email = MutableLiveData<String>()

    var password = MutableLiveData<String>()

    var repeatPassword = MutableLiveData<String>()

    private val auth = FirebaseAuth.getInstance()

    private var _registerProcess = MutableLiveData<Boolean>()
    val registerProcess: LiveData<Boolean>
        get() = _registerProcess

    private var _registerError = MutableLiveData<String>()
    val registerError: LiveData<String>
        get() = _registerError

/*    class Factory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                return RegisterViewModel(userRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }*/

    fun register() {
        val displayName = displayName.value
        val email = email.value
        val password = password.value
        val repeatPassword = repeatPassword.value
        if (
            validateDisplayName(displayName) &&
            validateEmail(email) &&
            validatePassword(password) &&
            validateRepeatPassword(repeatPassword) &&
            validateMatchingPasswords(password, repeatPassword) &&
            email != null && password != null
        ) {
            _registerProcess.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser = task.result!!.user!!
                        Log.d("$logKey.register", "You are registered successfully")
                        updateProfile(user)
                    } else {
                        _registerError.value = task.exception?.message.toString()
                        Log.d(logKey, task.exception!!.message.toString())
                    }
                    _registerProcess.value = false
                }
        }
    }

    private var _navigateToProfile = MutableLiveData<Boolean>()
    val navigateToProfile: LiveData<Boolean>
        get() = _navigateToProfile

    private fun updateProfile(user: FirebaseUser) {
        val displayName = displayName.value
        if (displayName != null) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()
            user.updateProfile(profileUpdates).addOnCompleteListener { update ->
                if (update.isSuccessful) {
                    //After registering the users data is passed to the UserRepository
                    val u = User(user.uid, user.email, user.displayName)
                    userRepository.addUser(u)
                    _navigateToProfile.value = true
                }
            }
        }
    }

    private var _displayNameError = MutableLiveData<String>()
    val displayNameError: LiveData<String>
        get() = _displayNameError

    private var _emailError = MutableLiveData<String>()
    val emailError: LiveData<String>
        get() = _emailError

    private var _passwordError = MutableLiveData<String>()
    val passwordError: LiveData<String>
        get() = _passwordError

    private var _repeatPasswordError = MutableLiveData<String>()
    val repeatPasswordError: LiveData<String>
        get() = _repeatPasswordError

    private fun validateDisplayName(value: String?): Boolean {
        if (!value.isNullOrEmpty() && value.isNotEmpty() && value.trim() != "") {
            _displayNameError.value = ""
            return true
        }
        _displayNameError.value = "Please enter a display name"
        return false
    }

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


    private fun validatePassword(value: String?): Boolean {
        if (!value.isNullOrEmpty() && value.isNotEmpty() && value.trim() != "") {
            _passwordError.value = ""
            return true
        }
        _passwordError.value = "Please enter a valid password"
        return false
    }


    private fun validateRepeatPassword(value: String?): Boolean {
        if (!value.isNullOrEmpty() && value.isNotEmpty() && value.trim() != "") {
            _repeatPasswordError.value = ""
            return true
        }
        _repeatPasswordError.value = "Please repeat your password here"
        return false
    }

    private fun validateMatchingPasswords(p1: String?, p2: String?): Boolean {
        if (p1 == p2) {
            _repeatPasswordError.value = ""
            return true
        }
        _repeatPasswordError.value = "The repeated password does not match"
        return false
    }
}

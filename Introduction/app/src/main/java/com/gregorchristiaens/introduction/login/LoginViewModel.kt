package com.gregorchristiaens.introduction.login

import android.util.Log
import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.gregorchristiaens.introduction.repository.UserRepository

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val logKey = "LoginViewModel"

    /**
     * [email] stores value entered in the email EditText.
     * The String is stored as a MutableLiveData so that the view can observe it.
     */
    val email = MutableLiveData<String>()

    /**
     * [password] stores value entered in the password EditText.
     * The String is stored as a MutableLiveData so that the view can observe it.
     */
    val password = MutableLiveData<String>()

    /**
     * An instance of FirebaseAuth is acquired using [FirebaseAuth.getInstance]
     * This ensures ensures we use a single instance of [FirebaseAuth] throughout the application.
     */
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * A private mutable instance of navigateToProfile
     * This LiveData holds whether or not the login was successful and the view can navigate forward.
     * The private [_navigateToProfile] is the variable we use to edit the value in the viewModel.
     * This should never be exposed to the view, fragment or activity.
     * When false the login failed and the ui should display an appropriate message.
     * When true the login passed and the ui should navigate forward.
     */
    private val _navigateToProfile = MutableLiveData<Boolean>()
    val navigateToProfile: LiveData<Boolean>
        get() = _navigateToProfile

    /**
     * init runs after the ViewModel is created.
     * We initialize the value of _navigateToProfile on the value false.
     */
    init {
        _navigateToProfile.value = false
    }

    /**
     * Login using Firebase with Email and Password
     * Uses [email] and [password] LiveData.
     * Uses [auth] initialized on init.
     */
    fun loginEmail() {
        val email = email.value
        val password = password.value
        if (validateEmail(email) && validatePassword(password) && !email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            Log.d(logKey, "Starting Login Email process")
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(logKey, "signInWithEmailAndPassword:success")
                        val user = auth.currentUser
                        if (user != null) {
                            userRepository.getUserData(user)
                        }
                        _navigateToProfile.value = true
                    } else {
                        val message = task.exception!!.message.toString()
                        Log.d(logKey, message)
                        if (message.contains("password")) _passwordError.value = message
                        else _emailError.value = message
                        //TODO this is in theory obsolete
                        _navigateToProfile.value = false
                    }
                }
        }
    }

    /**
     * The LivaData [emailError] is used in the ui to fill EditText.error.
     * A value is assigned on email validation and on the login attempt.
     * TODO see if an extra error is required for non email & password errors
     */
    private val _emailError = MutableLiveData<String>()
    val emailError: LiveData<String>
        get() = _emailError

    /**
     * Set the value of [emailError] when certain conditions are met:
     * * Email is empty or null
     * * Email only contains spaces
     * * Email is an empty string
     */
    private fun validateEmail(value: String?): Boolean {
        if (value.isNullOrEmpty() || value == "" || value.trim() == "") {
            _emailError.value = "Please enter your email address"
            return false
        }
        _emailError.value = ""
        return true
    }

    /**
     * The LivaData [passwordError] is used in the ui to fill EditText.error.
     * A value is assigned on password validation and on the login attempt.
     */
    private val _passwordError = MutableLiveData<String>()
    val passwordError: LiveData<String>
        get() = _passwordError

    /**
     * Set the value of [passwordError] when certain conditions are met:
     * * Password is empty or null
     * * Password only contains spaces
     * * Password is an empty string
     */
    private fun validatePassword(value: String?): Boolean {
        if (value.isNullOrEmpty() || value == "" || value.trim() == "") {
            _passwordError.value = "Please enter your password"
            return false
        }
        _passwordError.value = ""
        return true
    }

    private val _loginError = MutableLiveData<String>()
    val loginError: LiveData<String>
        get() = _loginError

    /**
     * After requesting the account token we can continue the Google account authentication in the ViewModel.
     * If the token request fails we will notify the user.
     * task.isSuccessful: the request was successful and whe proceed to request the login credentials.
     * Uses [GoogleAuthProvider] to get Credentials.
     * Uses [FirebaseAuth.signInWithCredential] to then sign in with those credentials.
     *
     * Catches:[ApiException]
     * * Notify the user that the Google Sign in failed.
     */
    fun loginGoogle(task: Task<GoogleSignInAccount>, exception: Exception?) {
        if (task.isSuccessful) {
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(logKey, "firebaseAuthWithGoogle:" + account.id)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { signInAttempt ->
                        if (signInAttempt.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(logKey, "signInWithCredential:success")
                            val user = auth.currentUser
                            if (user != null) {
                                Log.d(logKey, "Logged in user: ${user.email}")
                            }
                            _navigateToProfile.value = true
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(logKey, "signInWithCredential:failure", signInAttempt.exception)
                            //Update ui no user
                        }
                    }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(logKey, "Google sign in failed", e)
                _loginError.value = "Google sign in failed, please try again"
            }
        } else {
            Log.w(logKey, exception)
            _loginError.value =
                "Could not connect to Google Play Services"
        }

    }
}
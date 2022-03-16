package com.gregorchristiaens.introduction.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.gregorchristiaens.introduction.R
import com.gregorchristiaens.introduction.databinding.FragmentLoginBinding
import com.gregorchristiaens.introduction.repository.UserRepository


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class LoginFragment : Fragment() {

    private val logKey = "IntroductionApp.KEY.LoginFragment"

    private var _binding: FragmentLoginBinding? = null

    /**
     * This property is only valid between [onCreateView] and [onDestroyView].
     */
    private val binding get() = _binding!!

    /**
     * Create the viewmodel using by viewModels.
     * This will ensure the appropriate factory is used for its creation.
     * The viewmodel is cleared when the owner activity is finished or when the fragment is detached.
     * A great image representing the ViewModel lifecycle: https://developer.android.com/images/topic/libraries/architecture/viewmodel-lifecycle.png
     */
    private lateinit var viewModel: LoginViewModel
    private lateinit var viewModelFactory: LoginViewModelFactory

    private lateinit var googleSignInClient: GoogleSignInClient

    /**
     * Initiate the [_binding] property so that it stores the appropriate [FragmentLoginBinding].
     * Assign [viewModel] to the [binding] property so that our view access it.
     * Check documentation [viewModel] to check on the shared methods and properties.
     * MVVM : Model View ViewModel
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("293071422365-29lid66kgot2urhccq51vbjjv837fh9n.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        viewModelFactory = LoginViewModelFactory(UserRepository.getInstance())
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
        binding.viewmodel = viewModel

        return binding.root
    }

    /**
     * We know the [viewModel] has been bound to the view in [onCreateView].
     * [viewModel] holds an observable to identify when the user has successfully logged in.
     * In [onViewCreated] we observe this variable "navigateToProfile" and navigate once it returns true.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigateToProfile.observe(viewLifecycleOwner) {
            if (it) {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_loginFragment_to_profileFragment)
            }
        }
        viewModel.emailError.observe(viewLifecycleOwner) {
            binding.emailField.error = it
        }
        viewModel.passwordError.observe(viewLifecycleOwner) {
            binding.passwordField.error = it
        }
        viewModel.loginError.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
        binding.loginGoogle.setOnClickListener {
            signInGoogle()
        }
        binding.navigateToRegister.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.forgotPassword.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
    }

    /**
     * Let go of the stored [_binding] variable.
     * After [onDestroyView] is called this variable is no longer valid.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * [RC_SIGN_IN] hold a unique number that identifies the request
     *
     */
    companion object {
        const val RC_SIGN_IN = 120
    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        Log.d(logKey, "Starting google signIn intent")
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            viewModel.loginGoogle(task, exception)
        }
    }
}
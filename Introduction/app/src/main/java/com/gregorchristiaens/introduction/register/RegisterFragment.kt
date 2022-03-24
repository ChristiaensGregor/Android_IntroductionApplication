package com.gregorchristiaens.introduction.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.gregorchristiaens.introduction.repository.UserRepository
import com.gregorchristiaens.introduction.R
import com.gregorchristiaens.introduction.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private val logKey = "IntroductionApp.LOGKEY.RegisterFragment"

    private var _binding: FragmentRegisterBinding? = null

    /**
     * This property is only valid between [onCreateView] and [onDestroyView].
     */
    private val binding get() = _binding!!

    private lateinit var viewModelFactory: RegisterViewModelFactory
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFactory = RegisterViewModelFactory(UserRepository.getInstance())
        viewModel = ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.displayNameError.observe(viewLifecycleOwner) {
            binding.displayNameField.error = it
        }
        viewModel.emailError.observe(viewLifecycleOwner) {
            binding.emailField.error = it
        }
        viewModel.passwordError.observe(viewLifecycleOwner) {
            binding.passwordField.error = it
        }
        viewModel.repeatPasswordError.observe(viewLifecycleOwner) {
            binding.repeatPasswordField.error = it
        }
        viewModel.registerError.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
        viewModel.registerProcess.observe(viewLifecycleOwner) {
            if (it) binding.progressbar.visibility = View.VISIBLE
            else binding.progressbar.visibility = View.GONE
        }

        binding.login.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_registerFragment_to_loginFragment)
        }
        viewModel.navigateToProfile.observe(viewLifecycleOwner) {
            if (it) {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_registerFragment_to_menuFragment)
            }
        }
    }
}
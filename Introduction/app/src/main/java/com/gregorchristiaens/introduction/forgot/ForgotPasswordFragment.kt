package com.gregorchristiaens.introduction.forgot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.gregorchristiaens.introduction.R
import com.gregorchristiaens.introduction.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : Fragment() {

    private val logKey = "IntroductionApp.LOGKEY.ForgotPasswordFragment"

    private var _binding: FragmentForgotPasswordBinding? = null

    /**
     * This property is only valid between [onCreateView] and [onDestroyView].
     */
    private val binding get() = _binding!!

    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this)[ForgotPasswordViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.login.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
        }
        viewModel.resetSuccess.observe(viewLifecycleOwner) {
            if (it) {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
            }
        }
        viewModel.resetError.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
        viewModel.emailError.observe(viewLifecycleOwner) {
            binding.emailField.error = it
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
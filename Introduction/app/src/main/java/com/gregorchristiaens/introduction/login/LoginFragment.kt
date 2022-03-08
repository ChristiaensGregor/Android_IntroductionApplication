package com.gregorchristiaens.introduction.login

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.gregorchristiaens.introduction.R
import com.gregorchristiaens.introduction.databinding.FragmentLoginBinding


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    /**
     * This property is only valid between [onCreateView] and [onDestroyView].
     */
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val displayMetrics = resources.displayMetrics
        var width = displayMetrics.widthPixels / displayMetrics.density
        Log.d("LaunchActivity", "Width: $width")
        viewModel.navigateToProfile.observe(viewLifecycleOwner) {
            if (it) {
                if (width < 600) {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_loginFragment_to_profileFragment)
                } else {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_landingFragment_to_profileFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.gregorchristiaens.introduction.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.gregorchristiaens.introduction.R
import com.gregorchristiaens.introduction.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private val logKey = "IntroductionApp.KEY.ProfileFragment"

    private var _binding: FragmentProfileBinding? = null

    /**
     * This property is only valid between [onCreateView] and [onDestroyView].
     */
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigateToLanding.observe(viewLifecycleOwner) {
            if (it) {
                Log.d(logKey, "Navigating to Landing")
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_profileFragment_to_landingFragment)
            }
        }
    }
}
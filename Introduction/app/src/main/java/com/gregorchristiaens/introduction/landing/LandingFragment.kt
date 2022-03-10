package com.gregorchristiaens.introduction.landing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gregorchristiaens.introduction.R
import com.gregorchristiaens.introduction.databinding.FragmentLandingBinding
import com.gregorchristiaens.introduction.repository.UserRepository

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LandingFragment : Fragment() {

    private var _binding: FragmentLandingBinding? = null

    /**
     * This property is only valid between [onCreateView] and [onDestroyView].
     */
    private val binding get() = _binding!!

    private lateinit var viewModel: LandingViewModel
    private lateinit var viewModelFactory: LandingViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLandingBinding.inflate(inflater, container, false)
        viewModelFactory = LandingViewModelFactory(UserRepository.getInstance())
        viewModel = ViewModelProvider(this, viewModelFactory).get(LandingViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO navigate to login if not logged in , navigate past login if logged in user found
        if (viewModel.firebaseUser.value == null) {
            findNavController().navigate(R.id.action_landingFragment_to_loginFragment)
        } else {
            findNavController().navigate(R.id.action_landingFragment_to_profileFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
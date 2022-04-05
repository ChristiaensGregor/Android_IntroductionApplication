package com.gregorchristiaens.introduction.menu

import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.gregorchristiaens.introduction.R
import com.gregorchristiaens.introduction.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    private val logKey = "IntroductionApp.LOGKEY.MenuFragment"

    private var _binding: FragmentMenuBinding? = null

    private val binding get() = _binding!!

    private val viewModel: MenuViewModel by viewModels()

    private var connectivity: Connectivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        context?.let {
            connectivity = Connectivity(it)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigateToLanding.observe(viewLifecycleOwner) {
            if (it) {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_menuFragment_to_landingFragment)
            }
        }
        binding.profile.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_menuFragment_to_profileFragment)
        }
        binding.karate.setOnClickListener {
            if (viewModel.hasKarateClub.value == true) {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_menuFragment_to_lessonsFragment)
            } else {
                if (viewModel.checkKarateClub()) {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_menuFragment_to_lessonsFragment)
                } else {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_menuFragment_to_karateClubFragment)
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
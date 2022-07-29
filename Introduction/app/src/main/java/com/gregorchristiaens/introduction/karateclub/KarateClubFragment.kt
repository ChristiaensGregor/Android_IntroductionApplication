package com.gregorchristiaens.introduction.karateclub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.gregorchristiaens.introduction.R
import com.gregorchristiaens.introduction.databinding.FragmentKarateClubBinding

class KarateClubFragment : Fragment() {

    private val logKey = "IntroductionApp.LOGKEY.KarateClubFragment"

    private var _binding: FragmentKarateClubBinding? = null
    private val binding get() = _binding!!

    private val viewModel: KarateClubViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKarateClubBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.joinKarateClub.setOnClickListener {
            viewModel.joinKarateClub()
        }
        viewModel.navigateToLessons.observe(viewLifecycleOwner) {
            if (it) {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_karateClubFragment_to_lessonsFragment)
            }
        }
    }

}
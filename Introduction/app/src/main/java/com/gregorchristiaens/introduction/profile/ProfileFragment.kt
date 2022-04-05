package com.gregorchristiaens.introduction.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.gregorchristiaens.introduction.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private val logKey = "IntroductionApp.LOGKEY.ProfileFragment"

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
        viewModel.user.observe(viewLifecycleOwner) {
            if (it.karateClubId == null || it.karateClubId == "") {
                binding.leaveKarateClub.visibility = View.GONE
            }
            else{
                binding.leaveKarateClub.visibility = View.VISIBLE

            }
        }
    }
}
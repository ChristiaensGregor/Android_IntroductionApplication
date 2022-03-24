package com.gregorchristiaens.introduction.lesson

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.gregorchristiaens.introduction.databinding.FragmentLessonBinding


class LessonFragment : Fragment() {

    private val logKey = "IntroductionApp.LOGKEY.LessonFragment"

    private var _binding: FragmentLessonBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LessonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLessonBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.participateOn.observe(viewLifecycleOwner) {
            if (it) {
                binding.participate.visibility = View.VISIBLE
                binding.unParticipate.visibility = View.GONE
            } else {
                binding.participate.visibility = View.GONE
                binding.unParticipate.visibility = View.VISIBLE
            }
        }
        binding.participate.setOnClickListener {
            viewModel.participate()
        }
        binding.unParticipate.setOnClickListener {
            viewModel.unParticipate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
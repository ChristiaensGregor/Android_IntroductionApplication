package com.gregorchristiaens.introduction.lessons

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.gregorchristiaens.introduction.databinding.FragmentLessonsBinding


class LessonsFragment : Fragment() {

    private val logKey = "IntroductionApp.LOGKEY.LessonsFragment"

    private var _binding: FragmentLessonsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LessonsViewModel by viewModels()

    private var viewModelAdapter: LessonAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLessonsBinding.inflate(inflater, container, false)
        //binding.viewmodel = viewModel
        viewModelAdapter = LessonAdapter()
        binding.root.layoutManager = LinearLayoutManager(context)
        binding.root.adapter = viewModelAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.lessons.observe(viewLifecycleOwner) {
            it.apply { viewModelAdapter?.lessons = it }
        }
    }
}
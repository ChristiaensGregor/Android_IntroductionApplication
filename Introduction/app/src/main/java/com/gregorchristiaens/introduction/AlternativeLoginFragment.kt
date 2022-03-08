package com.gregorchristiaens.introduction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gregorchristiaens.introduction.databinding.FragmentAlternativeLoginBinding
import com.gregorchristiaens.introduction.databinding.FragmentLoginBinding

class AlternativeLoginFragment : Fragment() {


    private var _binding: FragmentAlternativeLoginBinding? = null

    /**
     * This property is only valid between [onCreateView] and [onDestroyView].
     */
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlternativeLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navigateToRegister.setOnClickListener {
            Toast.makeText(context, "Register", Toast.LENGTH_SHORT).show()
        }
        binding.loginGoogle.setOnClickListener {
            Toast.makeText(context, "Google Login", Toast.LENGTH_SHORT).show()
        }
        binding.loginGithub.setOnClickListener {
            Toast.makeText(context, "GitHub Login", Toast.LENGTH_SHORT).show()
        }
    }
}
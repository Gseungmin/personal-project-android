package com.umc.personal.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.umc.personal.databinding.FragmentMypageBinding
import com.umc.personal.ui.activity.LoginActivity
import com.umc.personal.ui.viewmodel.LoginViewModel

class MypageFragment : Fragment() {

    private var _binding : FragmentMypageBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.logout.setOnClickListener {
            viewModel.logout()
        }

        viewModel.logoutSuccess.observe(viewLifecycleOwner) {
            if (it) {
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
                requireActivity().finish()
            }
        }
        return view
    }
}
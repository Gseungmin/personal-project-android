package com.umc.personal.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.umc.personal.R
import com.umc.personal.data.dto.login.post.BasicLoginDto
import com.umc.personal.databinding.FragmentLoginBinding
import com.umc.personal.ui.activity.LoginActivity
import com.umc.personal.ui.activity.MainActivity
import com.umc.personal.ui.viewmodel.JoinViewModel
import com.umc.personal.ui.viewmodel.LoginViewModel
import java.util.regex.Pattern

class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        //이메일 유효성 검사
        validate_email()

        //회원가입 화면으로 이동
        join()

        //성공이면 메인 화면으로 이동
        viewModel.success.observe(viewLifecycleOwner) {
            if (it) {
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()
            }
        }

        return view
    }

    /**시작시 로그인 상태 확인 메소드*/
    override fun onStart() {
        super.onStart()
    }

    private fun validate_email() {
        binding.emailLoginButton.setOnClickListener {
            val pattern: Pattern = Patterns.EMAIL_ADDRESS

            val email = binding.email.text

            if (pattern.matcher(email).matches()) {
            }

            //basic_login
            viewModel.basic_login(BasicLoginDto(binding.email.text.toString(), binding.password.text.toString()))
        }
    }

    private fun join() {

        //회원가입 화면으로 이동
        binding.joinButton.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_loginFragment_to_joinFragment)
        }
    }

    /**
     * viewBinding이 더이상 필요 없을 경우 null 처리 필요
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
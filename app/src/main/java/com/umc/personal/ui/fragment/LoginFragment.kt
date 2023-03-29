package com.umc.personal.ui.fragment

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.personal.databinding.FragmentLoginBinding
import java.util.regex.Pattern

class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

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
        }
    }

    private fun join() {

        //회원가입 화면으로 이동
        binding.joinButton.setOnClickListener {
            
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
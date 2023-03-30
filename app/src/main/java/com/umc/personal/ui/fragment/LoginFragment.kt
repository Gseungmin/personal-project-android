package com.umc.personal.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import com.umc.personal.API.KAKAO_KEY
import com.umc.personal.R
import com.umc.personal.data.dto.login.post.BasicLoginDto
import com.umc.personal.databinding.FragmentLoginBinding
import com.umc.personal.ui.activity.LoginActivity
import com.umc.personal.ui.activity.MainActivity
import com.umc.personal.ui.viewmodel.JoinViewModel
import com.umc.personal.ui.viewmodel.LoginViewModel
import com.umc.personal.util.BlackToast
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

        viewModel.error.observe(requireActivity()) {
            BlackToast.createToast(requireContext(), it.message.toString()).show()
        }

        kakao_login()

        return view
    }

    /**시작시 로그인 상태 확인 메소드*/
    override fun onStart() {
        super.onStart()
    }

    private fun validate_email() {
        binding.emailLoginButton.setOnClickListener {
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

    //카카오 로그인 로직
    private fun kakao_login() {
        //Kakao SDK를 사용하기 위해선 Native App Key로 초기화
        KakaoSdk.init(requireContext(), KAKAO_KEY)

        //로그인 실패시 callback/
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(requireContext(), "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Toast.makeText(requireContext(), "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Toast.makeText(requireContext(), "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Toast.makeText(requireContext(), "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Toast.makeText(requireContext(), "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Toast.makeText(requireContext(), "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Toast.makeText(requireContext(), "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Toast.makeText(requireContext(), "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(requireContext(), "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (token != null) {
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                    }
                    else if (user != null) {
                        //로그인 진행
                        viewModel.kako_login(token.accessToken.toString())
                    }
                }
            }
        }

        //로그인 버튼 입력시 dialog 보여줌 그 후에 로그인 진행
        binding.kakaoLogin.setOnClickListener {

            val dialog = LayoutInflater.from(requireContext()).inflate(R.layout.social_kakao_login_dialog, null)
            val builder = AlertDialog.Builder(requireContext()).setView(dialog)

            val alertDialog = builder.show()

            //dialog의 view Component 접근
            val dialog_cancel = alertDialog.findViewById<TextView>(R.id.back)
            val keep_going = alertDialog.findViewById<TextView>(R.id.back_fragment)

            dialog_cancel.setOnClickListener {
                alertDialog.cancel()
            }

            keep_going.setOnClickListener {
                if(UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())){
                    UserApiClient.instance.loginWithKakaoTalk(context = requireContext(), callback = callback)
                }else{
                    UserApiClient.instance.loginWithKakaoAccount(context = requireContext(), callback = callback)
                }
                alertDialog.cancel()
            }
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
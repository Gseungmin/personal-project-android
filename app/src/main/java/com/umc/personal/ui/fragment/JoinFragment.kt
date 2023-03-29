package com.umc.personal.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.umc.personal.R
import com.umc.personal.data.dto.login.post.BasicJoinDto
import com.umc.personal.databinding.FragmentJoinBinding
import com.umc.personal.ui.activity.MainActivity
import com.umc.personal.ui.viewmodel.JoinViewModel
import java.util.regex.Pattern

class JoinFragment : Fragment() {

    private var _binding : FragmentJoinBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<JoinViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentJoinBinding.inflate(inflater, container, false)
        val view = binding.root

        //가입 눌렀을때 로직
        validation()

        //email 입력칸으로 이동
        back_to_login()

        //옵저버 패턴 관리
        observer()

        return view
    }

    /**observer pattern*/
    private fun observer() {

        //회원가입시 받은 데이터
        viewModel.join_state.observe(viewLifecycleOwner) {
            if (it == true) {
                Handler(Looper.myLooper()!!).postDelayed({
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }, 300)
            } else {
                dialog()
            }
        }
    }

    /**login fragment로 이동*/
    private fun back_to_login() {
        binding.backToEmailLogin.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_joinFragment_to_loginFragment)
        }
    }

    /**회원가입 유효성 체크*/
    private fun validation() {

        /**회원가입 체크 체크*/
        binding.join.setOnClickListener {

            //객체 생성
            val basicJoinDto = BasicJoinDto(
                binding.nickname.text.toString(),
                binding.email.text.toString(),
                binding.password.text.toString())

            //join
            viewModel.join(basicJoinDto)
        }
    }

    /**dialog를 보여주는 메소드*/
    private fun dialog() {
        val dialog = LayoutInflater.from(requireContext()).inflate(R.layout.join_fragment_dialog, null)
        val builder = AlertDialog.Builder(requireContext()).setView(dialog)

        val alertDialog = builder.show()

        //dialog의 view Component 접근
        val dialog_cancel = alertDialog.findViewById<TextView>(R.id.back)
        val back_to_login = alertDialog.findViewById<TextView>(R.id.back_fragment)
        val email = alertDialog.findViewById<TextView>(R.id.email_name)

        email.text = binding.email.text

        dialog_cancel.setOnClickListener {
            alertDialog.cancel()
        }

        back_to_login.setOnClickListener {
            alertDialog.cancel()
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
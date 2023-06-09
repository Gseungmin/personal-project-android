package com.umc.personal.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.umc.personal.databinding.ActivitySplashBinding
import com.umc.personal.ui.viewmodel.CheckTokenViewModel
import com.umc.personal.util.BlackToast

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel by viewModels<CheckTokenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel.checkAccessToken()

        viewModel.error.observe(this) {
            BlackToast.createToast(this, "로그인을 먼저 해주세요").show()
        }

        viewModel.accessToken.observe(this) {
            if (it == false) {
                Handler(Looper.myLooper()!!).postDelayed({
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }, 1500)
            } else {
                Handler(Looper.myLooper()!!).postDelayed({
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }, 1500)
            }
        }
    }
}
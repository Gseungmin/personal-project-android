package com.umc.personal.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umc.personal.databinding.ActivityMainBinding
import com.umc.personal.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}
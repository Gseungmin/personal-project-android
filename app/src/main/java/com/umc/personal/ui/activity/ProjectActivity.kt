package com.umc.personal.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umc.personal.databinding.ActivityProjectBinding

class ProjectActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProjectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProjectBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}
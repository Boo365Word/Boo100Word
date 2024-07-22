package com.example.boo345word.ui.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.boo345word.databinding.ActivityGameBinding

class GameActivity  : AppCompatActivity(){


    private lateinit var binding : ActivityGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.timeProgressBar.progress =50
    }

}
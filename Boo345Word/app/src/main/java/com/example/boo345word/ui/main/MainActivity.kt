package com.example.boo345word.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.boo345word.R
import com.example.boo345word.databinding.ActivityMainBinding
import com.example.boo345word.ui.game.GameActivity
import com.example.boo345word.ui.game.GameFragment
import com.example.boo345word.ui.word.WordListActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding  : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.heartProgressBar.setProgress(0.8f)

        binding.btnGameStart.setOnClickListener{
            // todo : 게임 시작 화면 뜨도록 하기
            val intent = Intent(this,GameActivity::class.java)
            startActivity(intent)

        }

        binding.btnWord.setOnClickListener {
            val intent = Intent(this, WordListActivity::class.java)
            startActivity(intent)
        }
    }
}
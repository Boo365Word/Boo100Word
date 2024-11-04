package com.example.boo345word.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.boo345word.R
import com.example.boo345word.databinding.ActivityMainBinding
import com.example.boo345word.ui.custom.InfoDialog
import com.example.boo345word.ui.game.GameActivity
import com.example.boo345word.ui.game.GameFragment
import com.example.boo345word.ui.word.WordListActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initMainBinding()
        initMainClickListener()
    }

    private fun initMainBinding(){
        binding = ActivityMainBinding.inflate(layoutInflater).also { binding ->
            setContentView(binding.root)
        }
    }

    private fun initMainClickListener(){
        with(binding) {
            heartProgressBar.setProgress(INITIAL_PROGRESS_VALUE)

            btnGameStart.setOnClickListener {
                GameActivity.start(this@MainActivity)
            }
            btnWord.setOnClickListener {
                WordListActivity.start(this@MainActivity)
            }
            btnInfo.setOnClickListener {
                infoOverlay.also { overlay ->
                    overlay.visibility = View.VISIBLE
                    InfoDialog(this@MainActivity).showViewPagerDialog(it)
                }
            }
        }
    }

    companion object{

        private const val INITIAL_PROGRESS_VALUE = 0.8f
    }
}
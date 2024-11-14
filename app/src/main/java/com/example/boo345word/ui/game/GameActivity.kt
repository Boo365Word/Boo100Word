package com.example.boo345word.ui.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.boo345word.R
import com.example.boo345word.databinding.ActivityGameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initGameBinding()

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.game_fragment_container_view, GameFragment.newInstance())
            }
        }
    }

    private fun initGameBinding() {
        binding = ActivityGameBinding.inflate(layoutInflater).also { binding -> setContentView(binding.root) }
    }

    companion object {
        fun start(context: Context) {
            Intent(context, GameActivity::class.java).also {
                context.startActivity(it)
            }
        }
    }
}

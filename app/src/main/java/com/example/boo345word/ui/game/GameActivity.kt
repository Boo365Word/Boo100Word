package com.example.boo345word.ui.game

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.boo345word.R
import com.example.boo345word.databinding.ActivityGameBinding
import com.example.boo345word.ui.custom.DrawingView

class GameActivity  : AppCompatActivity(){

    private lateinit var binding : ActivityGameBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (savedInstanceState ==null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.game_fragment_container_view,GameFragment.newInstance(1))
                .commit()
            }
        }

    fun skipState(currentState : Int) {
        Log.d("넘어가기","스킵><")
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .add(R.id.game_fragment_container_view,GameFragment.newInstance(currentState))
            .commit()
    }
}



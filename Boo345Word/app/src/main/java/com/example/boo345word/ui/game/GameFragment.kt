package com.example.boo345word.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.boo345word.R
import com.example.boo345word.databinding.ActivityGameBinding
import com.example.boo345word.databinding.FragmentGameBinding
import com.example.boo345word.ui.custom.GameResultDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GameFragment : Fragment() {

    private lateinit var binding : FragmentGameBinding
    private var  stateCount : Int = 5

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameBinding.inflate(inflater)
        Log.d("게임 프래그먼트당","욥")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //todo : timer와 연동하기 (30초)
        lifecycleScope.let {
            val currentState = 0

            binding.timeProgressBar.progress = currentState

        }

        binding.icEraser.setOnClickListener {
            binding.drawingView.clearCanvas()
        }

        val currentState = arguments?.getInt("currentState") ?: 0
        binding.txtCurrentState.text = currentState.toString()

        // 맞힌 경우
        if (binding.txtCurrentState.text =="6") {
            // todo : 게임 결과창 띄우기
        }
        binding.btnSkip.setOnClickListener {
            //틀린 경우
            if (binding.txtCurrentState.text ==stateCount.toString()) {
                // todo : 게임 결과창 띄우기
                showGameResult()
            }
            else{
                (activity as? GameActivity)?.skipState(currentState + 1)

            }
        }
        }

    private fun showGameResult(){
        context?.let {
            val dialog = GameResultDialog(it)
            dialog.show()
        }

    }

    companion object{
        fun newInstance(currentState: Int) : GameFragment {
            val args = Bundle().apply {
                putInt("currentState",currentState)
            }
            val fragment = GameFragment()
            fragment.arguments = args
            return fragment
        }
    }
    }


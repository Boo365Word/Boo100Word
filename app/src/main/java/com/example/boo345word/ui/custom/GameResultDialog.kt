package com.example.boo345word.ui.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.boo345word.R
import com.example.boo345word.data.entity.BasicWord
import com.example.boo345word.databinding.DialogGameResultBinding
import com.example.boo345word.ui.game.adapter.GameResultCorrectListAdapter
import com.example.boo345word.ui.game.adapter.GameResultWrongListAdapter
import com.example.boo345word.ui.main.MainActivity

class GameResultDialog(
    private val context: Context,
    private val correctWordList: List<BasicWord>,
    private val wrongWordList: List<BasicWord>,
    private val listener: GameResultDialogListener
) : DialogFragment() {

    private var _binding: DialogGameResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogGameResultBinding.inflate(inflater, container, false)
        val view = binding.root

        initGameResultDialog()

        return view
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        initGameResultDialogView()
        initGameResultListener()
    }

    private fun initGameResultDialog() {
        dialog?.window?.let { window ->
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.setGravity(Gravity.CENTER)
        }
        isCancelable = false
        requireActivity().setFinishOnTouchOutside(true)
    }

    private fun initGameResultDialogView() {
        with(binding) {
            listCorrectWord.adapter = GameResultCorrectListAdapter(correctWordList)
            listWrongWord.adapter = GameResultWrongListAdapter(wrongWordList)
            imageView7.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            txtGameResult.updateText(correctWordList.size)
        }
        fillHeartsOnCorrectWords()
    }

    private fun fillHeartsOnCorrectWords() {
        val hearts = listOf(
            binding.heart1,
            binding.heart2,
            binding.heart3,
            binding.heart4,
            binding.heart5
        )
        repeat(correctWordList.size) { idx ->
            hearts[idx].setImageResource(R.drawable.ic_filled_heart)
        }
    }

    private fun initGameResultListener() {
        binding.btnGoFirst.setOnClickListener {
            dismiss()
            MainActivity.start(context)
        }
        binding.btnRetry.setOnClickListener {
            this.listener.onRetryGame()
            dismiss()
        }
    }
}

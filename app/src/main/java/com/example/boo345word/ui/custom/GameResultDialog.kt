package com.example.boo345word.ui.custom

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.boo345word.R
import com.example.boo345word.data.entity.BasicWord
import com.example.boo345word.databinding.DialogGameResultBinding
import com.example.boo345word.ui.game.adapter.GameResultCorrectListAdapter
import com.example.boo345word.ui.game.adapter.GameResultWrongListAdapter
import com.example.boo345word.ui.main.MainActivity

class GameResultDialog(
    correctWordList: List<BasicWord>,
    wrongWordList: List<BasicWord>,
    listener: GameResultDialogListener
) : DialogFragment() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: DialogGameResultBinding? = null
    private val binding get() = _binding!!
    private var correctWordList: List<BasicWord>? = null
    private var wrongWordList: List<BasicWord>? = null
    private var listener: GameResultDialogListener? = null

    init {
        this.correctWordList = correctWordList
        this.wrongWordList = wrongWordList
        this.listener = listener
    }

    interface GameResultDialogListener {
        fun onRetryGame()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogGameResultBinding.inflate(inflater, container, false)
        val view = binding.root
        requireActivity().window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false

        binding.btnGoFirst.setOnClickListener {
            Toast.makeText(context, "메인 화면으로 이동합니다", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            dismiss()
        }
        binding.btnRetry.setOnClickListener {
            this.listener?.onRetryGame()
            dismiss()
        }

        return view
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        requireActivity().setFinishOnTouchOutside(true)

        val gameResultCorrectAdapter = correctWordList?.let { GameResultCorrectListAdapter(it) }
        val gameResultWrongAdapter = wrongWordList?.let { GameResultWrongListAdapter(it) }
        binding.listCorrectWord.adapter = gameResultCorrectAdapter
        binding.listWrongWord.adapter = gameResultWrongAdapter
        binding.txtCorrectWordCount.text = correctWordList?.size.toString()
        binding.imageView7.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // 배경을 투명하게 해줌
        dialog?.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog?.window?.setGravity(Gravity.CENTER)

        when (correctWordList?.size) {
            1 -> {
                binding.heart1.setImageResource(R.drawable.ic_filled_heart)
            }

            2 -> {
                binding.heart1.setImageResource(R.drawable.ic_filled_heart)
                binding.heart2.setImageResource(R.drawable.ic_filled_heart)
            }

            3 -> {
                binding.heart1.setImageResource(R.drawable.ic_filled_heart)
                binding.heart2.setImageResource(R.drawable.ic_filled_heart)
                binding.heart3.setImageResource(R.drawable.ic_filled_heart)
            }

            4 -> {
                binding.heart1.setImageResource(R.drawable.ic_filled_heart)
                binding.heart2.setImageResource(R.drawable.ic_filled_heart)
                binding.heart3.setImageResource(R.drawable.ic_filled_heart)
                binding.heart4.setImageResource(R.drawable.ic_filled_heart)
            }

            5 -> {
                binding.heart1.setImageResource(R.drawable.ic_filled_heart)
                binding.heart2.setImageResource(R.drawable.ic_filled_heart)
                binding.heart3.setImageResource(R.drawable.ic_filled_heart)
                binding.heart4.setImageResource(R.drawable.ic_filled_heart)
                binding.heart5.setImageResource(R.drawable.ic_filled_heart)
            }
        }
    }
}

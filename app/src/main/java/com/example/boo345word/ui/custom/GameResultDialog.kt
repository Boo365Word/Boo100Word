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
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: DialogGameResultBinding? = null
    private val binding get() = _binding!!

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

        initGameResultListener()

        return view
    }

    private fun initGameResultListener() {
        // todo : 처음으로 돌아가기
        binding.btnGoFirst.setOnClickListener {
            dismiss()
            MainActivity.start(context)
        }
        binding.btnRetry.setOnClickListener {
            this.listener.onRetryGame()
            dismiss()
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        requireActivity().setFinishOnTouchOutside(true)

        val gameResultCorrectAdapter = correctWordList?.let { GameResultCorrectListAdapter(it) }
        val gameResultWrongAdapter = wrongWordList?.let { GameResultWrongListAdapter(it) }
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.listCorrectWord.adapter = gameResultCorrectAdapter
        binding.listWrongWord.adapter = gameResultWrongAdapter
        binding.imageView7.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.txtGameResult.updateText(correctWordList.size)

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

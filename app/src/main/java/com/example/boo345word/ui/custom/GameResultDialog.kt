package com.example.boo345word.ui.custom

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.boo345word.data.model.WordInfo
import com.example.boo345word.databinding.DialogGameResultBinding
import com.example.boo345word.ui.game.adapter.GameResultCorrectListAdapter
import com.example.boo345word.ui.game.adapter.GameResultWrongListAdapter
import com.example.boo345word.ui.main.MainActivity

class GameResultDialog(
    correctWordList: List<WordInfo>,
    wrongWordList: List<WordInfo>,
    listener: GameResultDialogListener,
) : DialogFragment() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: DialogGameResultBinding? = null
    private val binding get() = _binding!!
    private var correctWordList: List<WordInfo>? = null
    private var wrongWordList: List<WordInfo>? = null
    private var listener: GameResultDialogListener? = null

    init {
        this.correctWordList = correctWordList
        this.wrongWordList = wrongWordList
        this.listener = listener
    }

    interface GameResultDialogListener {
        fun onGoHome()

        fun onRetryGame()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogGameResultBinding.inflate(inflater, container, false)
        val view = binding.root
        requireActivity().window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.btnGoFirst.setOnClickListener {
            this.listener?.onGoHome()
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
        savedInstanceState: Bundle?,
    ) {
        requireActivity().setFinishOnTouchOutside(true)

        val gameResultCorrectAdapter = correctWordList?.let { GameResultCorrectListAdapter(it) }
        val gameResultWrongAdapter = wrongWordList?.let { GameResultWrongListAdapter(it) }
        binding.listCorrectWord.adapter = gameResultCorrectAdapter
        binding.listWrongWord.adapter = gameResultWrongAdapter
    }
}

package com.example.boo345word.ui.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.example.boo345word.databinding.DialogGameResultBinding
import com.example.boo345word.ui.game.GameActivity
import com.example.boo345word.ui.main.MainActivity

class GameResultDialog(context: Context) : Dialog(context) {

    private lateinit var binding: DialogGameResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initGameResultBinding()
        initGameResultListener()
    }

    private fun initGameResultBinding() {
        binding = DialogGameResultBinding.inflate(layoutInflater).also { binding ->
            setContentView(binding.root)
        }
        // 배경을 투명하게
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun initGameResultListener() {
        // todo : 처음으로 돌아가기
        binding.btnGoFirst.setOnClickListener {
            dismiss()
            MainActivity.start(context)
        }
        // todo : 다시 하기
        binding.btnRetry.setOnClickListener {
            dismiss()
            GameActivity.start(context)
        }
        // 다이얼로그 바깥쪽 클릭시 종료되도록 함
        setCanceledOnTouchOutside(true)
    }
}

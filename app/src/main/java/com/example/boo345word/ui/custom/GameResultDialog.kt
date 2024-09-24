package com.example.boo345word.ui.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.app.DialogCompat
import com.example.boo345word.databinding.DialogGameResultBinding

class GameResultDialog(context : Context) : Dialog(context) {

    private lateinit var binding : DialogGameResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogGameResultBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        // 배경을 투명하게
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // 다이얼로그 바깥쪽 클릭시 종료되도록 함
        setCanceledOnTouchOutside(true)

        //todo : 처음으로 돌아가기
        binding.btnGoFirst.setOnClickListener {

        }

        //todo : 다시 하기
        binding.btnRetry.setOnClickListener {

        }

    }



}
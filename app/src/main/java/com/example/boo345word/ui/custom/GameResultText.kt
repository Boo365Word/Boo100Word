package com.example.boo345word.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.boo345word.databinding.CustomResultTextBinding

class GameResultText(
    context: Context,
    attrs: AttributeSet,
) : ConstraintLayout(context, attrs) {
    private val binding = CustomResultTextBinding.inflate(LayoutInflater.from(context), this, true)

    fun updateText(correctCount: Int) {
        binding.txtResultText.text = "5단어 중 ${correctCount}개를 맞췄어요!"
    }
}

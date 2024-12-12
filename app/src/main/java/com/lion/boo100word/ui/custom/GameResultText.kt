package com.lion.boo100word.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.lion.boo100word.R
import com.lion.boo100word.databinding.CustomResultTextBinding

class GameResultText(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs) {

    private val binding = CustomResultTextBinding.inflate(LayoutInflater.from(context), this, true)

    fun updateText(correctCount: Int) {
        binding.txtResultText.text =
            context.getString(R.string.gameResultText_description).format(correctCount)
    }
}

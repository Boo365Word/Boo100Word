package com.lion.boo100word.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.lion.boo100word.databinding.HeartProgressBarBinding
import kotlin.math.round

class HeartProgressBar(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs) {

    private val binding = HeartProgressBarBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    ).also {
        it.ivProgressContainer.measure(
            MeasureSpec.UNSPECIFIED,
            MeasureSpec.UNSPECIFIED
        )
    }

    fun updateProgress(progressRate: Float) {
        val height = round(resources.displayMetrics.density * PROGRESS_BAR_HEIGHT).toInt()
        val containerWidth = binding.ivProgressContainer.measuredWidth

        binding.vProgressBar.layoutParams = LayoutParams(0, height).apply {
            topToTop = LayoutParams.PARENT_ID
            bottomToBottom = LayoutParams.PARENT_ID
            startToStart = LayoutParams.PARENT_ID
            endToEnd = LayoutParams.PARENT_ID
            marginStart = (MIN_MARGIN_RATE * containerWidth - MIN_MARGIN_RATE * containerWidth * progressRate).toInt()
        }
    }

    companion object {

        private const val PROGRESS_BAR_HEIGHT = 32
        private const val MIN_MARGIN_RATE = 0.8
    }
}

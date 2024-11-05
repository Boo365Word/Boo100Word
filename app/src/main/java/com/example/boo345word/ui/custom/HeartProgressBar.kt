package com.example.boo345word.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.boo345word.databinding.HeartProgressBar2Binding
import kotlin.math.round


class HeartProgressBar(
    context: Context,
    attrs: AttributeSet,
) : ConstraintLayout(context, attrs) {

    private val binding = HeartProgressBar2Binding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun updateProgress(progressRate: Float) {
        binding.ivProgressContainer.measure(
            MeasureSpec.UNSPECIFIED,
            MeasureSpec.UNSPECIFIED
        )
        val height = round(resources.displayMetrics.density * PROGRESS_BAR_HEIGHT).toInt()
        val containerWidth = binding.ivProgressContainer.measuredWidth

        binding.vProgressBar.layoutParams = LayoutParams(0, height).apply {
            topToTop = LayoutParams.PARENT_ID
            bottomToBottom = LayoutParams.PARENT_ID
            startToStart = LayoutParams.PARENT_ID
            endToEnd = LayoutParams.PARENT_ID
            marginStart = (0.8 * containerWidth - 0.8 * containerWidth * progressRate).toInt()
            Log.d("woogi", "updateProgress: $marginStart")
        }
    }

    companion object {

        private const val PROGRESS_BAR_HEIGHT = 32
    }
}
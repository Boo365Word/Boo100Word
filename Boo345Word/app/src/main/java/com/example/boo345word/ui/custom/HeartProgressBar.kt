package com.example.boo345word.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.boo345word.R

class HeartProgressBar @JvmOverloads constructor(
    context: Context,
    attrs : AttributeSet? = null,
    defStyleAttr : Int = 0,
) : ConstraintLayout(context,attrs,defStyleAttr){

    private lateinit var backgroundBar: View
    private lateinit var foregroundBar: View
    private lateinit var circularKnob: ImageView
    private val hearts = mutableListOf<ImageView>()

    private var maxProgress =100
    private var currentProgress = 0

    init {
        initView()
    }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.heart_progress_bar,this,true )

        backgroundBar = findViewById(R.id.backgroundBar)
        foregroundBar = findViewById(R.id.foregroundBar)
        circularKnob = findViewById(R.id.circularKnob)

        updateProgressBar(0f)
    }

    fun setProgress(progress: Int) {
        currentProgress = progress.coerceIn(0, maxProgress)
        val progressRatio = currentProgress.toFloat()/ maxProgress
        updateProgressBar(progressRatio)
    }
    private fun updateProgressBar(progressRatio : Float) {
        val layoutParams = foregroundBar.layoutParams as ViewGroup.LayoutParams
        layoutParams.width  = (backgroundBar.width * progressRatio).toInt()
        foregroundBar.layoutParams = layoutParams

        circularKnob.x = backgroundBar.x + (backgroundBar.width - circularKnob.width) * progressRatio

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateProgressBar(currentProgress.toFloat() / maxProgress)
    }

}
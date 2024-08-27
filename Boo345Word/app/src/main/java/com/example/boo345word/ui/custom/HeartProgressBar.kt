package com.example.boo345word.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.provider.CalendarContract.Colors
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.plus
import com.example.boo345word.R

class HeartProgressBar @JvmOverloads constructor(
    context: Context,
    attrs : AttributeSet? = null,
    defStyleAttr : Int = 0,
) : View(context,attrs,defStyleAttr){

    private val backgroundDrawable: Drawable?
    private val hearts = mutableListOf<ImageView>()
    private var progress = 0f
    private val cornerRadius = 40f // Adjust this value to change the roundness of the corners


    private var maxProgress =100
        set(value) {
            field = value
            invalidate() // Redraw view when max progress changes
        }
    private var currentProgress = 0
        set(value) {
            field = value
            progress = value.toFloat() / maxProgress // Update progress as percentage
            invalidate() // Redraw view when progress changes
        }

    init {
        backgroundDrawable = ContextCompat.getDrawable(context,R.drawable.level_up_bar_background)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        backgroundDrawable?.setBounds(0,0,width,height)
        backgroundDrawable?.draw(canvas)


        val paint = Paint().apply {
            color = Color.parseColor("#FF5050")
            color=Color.argb(200,Color.red(color),Color.green(color), Color.blue(color))
            isAntiAlias = true
        }


        val progressWidth = width * progress
        val top =60f
        val bottom = 135f

        val rectF = RectF(12f,top,progressWidth-5f,bottom)
             val rectF2 = RectF(790f,13f,988f,185f)

            canvas.drawRoundRect(rectF,cornerRadius,cornerRadius,paint)
            canvas.drawRoundRect(rectF2, 70f, 70f, paint)
    }



    fun setProgress(value: Float) {
        progress = value.coerceIn(0f,1f)
        invalidate()
    }

}
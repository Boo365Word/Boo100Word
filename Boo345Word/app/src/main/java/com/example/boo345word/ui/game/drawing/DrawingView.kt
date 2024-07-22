package com.example.boo345word.ui.game.drawing

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context : Context, attrs : AttributeSet) : View(context,attrs){
    private var path = Path()
    private val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 5f
        isAntiAlias = true
    }

    private val paths = mutableListOf<Path>()
    private lateinit var drawingMounds : RectF


    init {
        // todo :  초기화
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawingMounds = RectF(0f,0f,width.toFloat(),height.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas?.let {
            for (p in paths) {
                it.drawPath(p,paint)
            }
            it.drawPath(path,paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            val x = it.x
            val y = it.y

            if (drawingMounds.contains(x,y)){
                when(event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        path.moveTo(x,y)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        path.lineTo(x,y)
                        invalidate()
                    }
                    MotionEvent.ACTION_UP -> {
                        paths.add(Path(path))
                        path.reset()
                    }
                }
                return  true
            }
        }
        return false
    }


}
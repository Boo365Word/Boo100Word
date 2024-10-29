package com.example.boo345word.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class DrawingView @JvmOverloads constructor(
    context : Context,
    attrs : AttributeSet?= null,
    defStyleAttr : Int = 0,
) : View(context,attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = 0xFF0000FF.toInt() // 파란색
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 10f
    }

    private var path = Path()
    private val paths = mutableListOf<Path>() // 여러 경로를 저장하기 위한 리스트
    private val undonePaths = mutableListOf<Path>() // 실행 취소를 위한 리스트

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.d("ondraw 호출","onDraw")

        for (p in paths) {
            canvas.drawPath(p,paint)
        }
        Log.d("path는 이것!! ", path.toString())
        canvas.drawPath(path,paint)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {

            // 화면에 손가락이 닿은 채로 움직이는 중
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(event.x,event.y)
            }
            // 화면에 손가락이 닿음 채로 움직일 때
            MotionEvent.ACTION_DOWN -> {
                path = Path()
                path.moveTo(event.x,event.y)
                return true

            }

            // 화면에서 손가락을 댐
            MotionEvent.ACTION_UP -> {
                path.lineTo(event.x,event.y)
                paths.add(path)
                path = Path()

            }
        }

        // 화면갱신이 필요할 때 호출하는 함수
        // 지금 화면은 무효이니 다시 그려주세요 라는 의미
        invalidate()
        return true
    }

    fun clearCanvas() {
        paths.clear()
        Log.d("clear 호출",paths.toString())
        undonePaths.clear()
        path = Path()
        invalidate()
    }
}
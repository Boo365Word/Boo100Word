package com.example.boo345word.ui.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.io.File
import java.io.FileOutputStream

class DrawingView @JvmOverloads constructor(
    context : Context,
    attrs : AttributeSet?= null,
    defStyleAttr : Int = 0,
) : View(context,attrs, defStyleAttr) {
    private val strokes = mutableListOf<MutableList<Pair<Float, Float>>>()
    private var currentStroke = mutableListOf<Pair<Float, Float>>()
    private var minX = Float.MAX_VALUE
    private var minY = Float.MAX_VALUE
    private var maxX = Float.MIN_VALUE
    private var maxY = Float.MIN_VALUE



    private val handler = android.os.Handler(Looper.getMainLooper())
    private var drawingCompleteRunnable : Runnable? = null

    private val paint = Paint().apply {
//        color = 0xFF0000FF.toInt() // 파란색
                color = Color.BLACK
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 20f
    }

    private var path = Path()
    private val paths = mutableListOf<Path>() // 여러 경로를 저장하기 위한 리스트
    private val undonePaths = mutableListOf<Path>() // 실행 취소를 위한 리스트
    private var onDrawingCompletedListener: ((Bitmap) -> Unit)? = null
    fun setOnDrawingCompletedListener(listener: (Bitmap) -> Unit) {
        onDrawingCompletedListener = listener
    }

    fun getBitmap(): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE)
//        canvas.translate(0f, 80F) // Adjust this value to cut off more or less from the top
        draw(canvas)
        return returnedBitmap
    }
    private fun updateBounds(x: Float, y: Float) {
        minX = minOf(minX, x)
        minY = minOf(minY, y)
        maxX = maxOf(maxX, x)
        maxY = maxOf(maxY, y)

    }

    fun getStrokes(): List<List<Pair<Float, Float>>> {
        return strokes
    }

    fun getStrokesData(): Pair<List<List<List<Int>>>, List<Int>> {
        val strokesData = strokes.map { stroke ->
            listOf(
                stroke.map { it.first.toInt() },
                stroke.map { it.second.toInt() }
            )
        }
        val box = listOf(minX.toInt(), minY.toInt(), maxX.toInt(), maxY.toInt())
        return strokesData to box
    }

    fun saveBitmapToFile(bitmap: Bitmap, filename : String) {
        try {
            val file = File(context.filesDir, filename)
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG,100,out)
                Log.d("DrawingView", "Drawing saved to $filename")
            }
        } catch (e: Exception) {
            Log.e("DrawingView", "Failed to save drawing", e)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (p in paths) {
            canvas.drawPath(p,paint)
        }
        canvas.drawPath(path,paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.actionMasked
        val x = event.x
        val y = event.y

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                path = Path()
                path.moveTo(x, y)
                currentStroke = mutableListOf()
                currentStroke.add(x to y)
                updateBounds(x, y)
                drawingCompleteRunnable?.let { handler.removeCallbacks(it) }
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                currentStroke.add(x to y)
                updateBounds(x, y)
                drawingCompleteRunnable?.let { handler.removeCallbacks(it) }
            }
            MotionEvent.ACTION_UP -> {
                path.lineTo(x, y)
                paths.add(path)
                strokes.add(currentStroke)
                drawingCompleteRunnable = Runnable {
                    val bitmap = getBitmap()
                    onDrawingCompletedListener?.invoke(bitmap)
                }
                handler.postDelayed(drawingCompleteRunnable!!, 1000)
            }
        }

        invalidate()
        return true
    }
        fun clearCanvas() {
            paths.clear()
            strokes.clear()
            Log.d("clear 호출", paths.toString())
            undonePaths.clear()
            path.reset()
            minX = Float.MAX_VALUE
            minY = Float.MAX_VALUE
            maxX = Float.MIN_VALUE
            maxY = Float.MIN_VALUE
            invalidate()
        }

    fun recreateDrawingFromStrokes(strokes: List<List<List<Int>>>, box: List<Int>, width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        val paint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 20f
        }

        val scaleX = width.toFloat() / (box[2] - box[0])
        val scaleY = height.toFloat() / (box[3] - box[1])

        for (stroke in strokes) {
            val path = Path()
            path.moveTo(
                (stroke[0][0] - box[0]) * scaleX,
                (stroke[1][0] - box[1]) * scaleY
            )
            for (i in 1 until stroke[0].size) {
                path.lineTo(
                    (stroke[0][i] - box[0]) * scaleX,
                    (stroke[1][i] - box[1]) * scaleY
                )
            }
            canvas.drawPath(path, paint)
        }

        return bitmap
    }


}
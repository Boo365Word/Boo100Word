package com.example.boo345word.ui.classifier

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class QuickDrawClassifier(private val context : Context) {
    private var interpreter : Interpreter? = null

    init {
        val model = loadModelFile("model.tflite")
        val options = Interpreter.Options()
        interpreter = Interpreter(model,options)
    }

    private fun loadModelFile(modelName : String) : MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY , startOffset, declaredLength)
    }

    fun classifyDrawing(bitmap : Bitmap) : String {
        val inputArray = preprocessImage(bitmap)
        val outputArray = Array(1) {FloatArray(NUM_CLASSES)}
        interpreter?.run(inputArray,outputArray)
        return interpretOutput(outputArray[0])

    }

    private fun preprocessImage(bitmap: Bitmap) : Array<Array<FloatArray>> {
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap,28,28,true)
        val inputArray = Array(1) { Array(28) {FloatArray(28)} }

        for(x in 0 until  28){
            for(y in 0 until 28) {
                inputArray[0][x][y] = (Color.red(resizedBitmap.getPixel(x,y)) /255.0f )
            }
        }
        return inputArray
    }

    private fun interpretOutput(output : FloatArray) : String {
        val maxIndex = output.indices.maxByOrNull { output[it] } ?: -1
        return LABELS[maxIndex]
    }

    companion object {
        private val LABELS = arrayOf("apple", "banana", "cat", "dog", "elephant") // 예시 레이블
        private const val NUM_CLASSES = 5 // 클래스 수
    }
}


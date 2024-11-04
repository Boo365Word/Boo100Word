package com.example.boo345word.ui.classifier

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import org.json.JSONObject
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class QuickDrawClassifier(private val context: Context) {
    private val quickDrawJsonFile: JSONObject
    private var interpreter: Interpreter? = null


    init {
        interpreter = Interpreter(loadModelFile())
        val labelsJson = loadJSONFromAsset("quickdraw_labels.json")
        quickDrawJsonFile = JSONObject(labelsJson)
    }

    companion object {
        private const val LOG_TAG = "QuickDrawClassifier"
        private const val MODEL_NAME = "model.tflite"
        const val IMG_SIZE = 28
        private const val NUM_CLASSES = 345
    }


    private fun loadJSONFromAsset(jsonFilename: String): String {
        return context.assets.open(jsonFilename).bufferedReader().use { it.readText() }
    }

    fun classifyStrokes(strokes: List<List<List<Int>>>, box: List<Int>): List<Pair<String, Float>> {
        val transformedBitmap = transformImage(strokes, box)
        return predict(transformedBitmap)
    }

    private fun transformImage(strokes: List<List<List<Int>>>, box: List<Int>): Bitmap {
        Log.d("box 정보","${box[0]}, ${box[1]}, ${box[2]}, ${box[3]}")
        val width = box[2] - box[0]
        val height = box[3] - box[1]

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        val paint = Paint().apply {
            color = Color.BLACK
            strokeWidth = 3f
            style = Paint.Style.STROKE
        }

        for (stroke in strokes) {
            val path = Path()
            path.moveTo((stroke[0][0] - box[0]).toFloat(), (stroke[1][0] - box[1]).toFloat())
            for (i in 1 until stroke[0].size) {
                path.lineTo((stroke[0][i] - box[0]).toFloat(), (stroke[1][i] - box[1]).toFloat())
            }
            canvas.drawPath(path, paint)
        }

        return Bitmap.createScaledBitmap(bitmap, IMG_SIZE, IMG_SIZE, true)
    }

    private fun preprocessImage(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * IMG_SIZE * IMG_SIZE).apply {
            order(ByteOrder.nativeOrder())
        }

        val intValues = IntArray(IMG_SIZE * IMG_SIZE)
        bitmap.getPixels(intValues, 0, IMG_SIZE, 0, 0, IMG_SIZE, IMG_SIZE)

        for (pixelValue in intValues) {
            val grayScale = (Color.red(pixelValue) * 0.299f + Color.green(pixelValue) * 0.587f + Color.blue(pixelValue) * 0.114f) / 255.0f
            byteBuffer.putFloat(1.0f - grayScale)
        }

        return byteBuffer
    }

     private fun predict(bitmap: Bitmap): List<Pair<String, Float>> {
        val input = preprocessImage(bitmap)
        val output = Array(1) { FloatArray(NUM_CLASSES) }

         interpreter?.run(input, output)

        return output[0].mapIndexed { index, probability ->
            quickDrawJsonFile.getString(index.toString()) to probability
        }.sortedByDescending { it.second }.take(3)
    }

    private fun loadModelFile(): MappedByteBuffer {
        return context.assets.openFd(MODEL_NAME).use { fileDescriptor ->
            FileInputStream(fileDescriptor.fileDescriptor).use { inputStream ->
                inputStream.channel.map(
                    FileChannel.MapMode.READ_ONLY,
                    fileDescriptor.startOffset,
                    fileDescriptor.declaredLength
                )
            }
        }
    }


    fun logTop3Results(results: List<Pair<String, Float>>) {
        for ((label, probability) in results) {
            Log.d(LOG_TAG, "Prediction: $label with probability: ${probability * 100}%")
        }
    }
}
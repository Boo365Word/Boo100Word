package com.example.boo345word.ui.classifier

import android.app.Activity
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.*

class DrawDetector(activity: Activity) {
    private val TAG = this.javaClass.simpleName
    private var tflite: Interpreter? = null
    private lateinit var labelList: List<String>
    private var inputBuffer: ByteBuffer? = null
    private var mnistOutput: Array<FloatArray>? = null

    // Name of the files in the assets folder
    private val MODEL_PATH = "model (1004).tflite"
    private val LABEL_PATH = "class_names (1004).txt"
    private val RESULTS_TO_SHOW = 1

    // Output size
    private val NUMBER_LENGTH = 100

    // Input size
    private val DIM_BATCH_SIZE = 1
    private val DIM_IMG_SIZE_X = 28
    private val DIM_IMG_SIZE_Y = 28
    private val DIM_PIXEL_SIZE = 1
    private val BYTE_SIZE_OF_FLOAT = 4

    private val sortedLabels = PriorityQueue<Map.Entry<String, Float>>(
        RESULTS_TO_SHOW
    ) { o1, o2 -> o1.value.compareTo(o2.value) }

    init {
        try {
            tflite = Interpreter(loadModelFile(activity))
            labelList = loadLabelList(activity)
            inputBuffer = ByteBuffer.allocateDirect(
                BYTE_SIZE_OF_FLOAT * DIM_BATCH_SIZE * DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y * DIM_PIXEL_SIZE
            ).apply { order(ByteOrder.nativeOrder()) }
            mnistOutput = Array(DIM_BATCH_SIZE) { FloatArray(NUMBER_LENGTH) }
            Log.d(TAG, "Created a TensorFlow Lite MNIST Classifier.")
        } catch (e: IOException) {
            Log.e(TAG, "IOException loading the tflite file", e)
        }
    }

    /** Run the TFLite model */
    private fun runInference() {
        tflite?.run(inputBuffer, mnistOutput)
    }

    /** Classifies the number with the mnist model. */
    fun classify(bitmap: Bitmap?): String {
        if (tflite == null) {
            Log.e(TAG, "Image classifier has not been initialized; Skipped.")
            return ""
        }
        preprocess(bitmap)
        runInference()
        return printTopKLabels()
    }

    /** Go through the output and find the number that was identified. */
    private fun postprocess(): Int {
        mnistOutput?.get(0)?.forEachIndexed { i, value ->
            Log.d(TAG, "Output for $i: $value")
            if (value == 1f) return i
        }
        return -1
    }

    private fun printTopKLabels(): String {
        mnistOutput?.get(0)?.let {
            it.forEachIndexed { i, value ->
                sortedLabels.add(AbstractMap.SimpleEntry(labelList[i], value))
                if (sortedLabels.size > RESULTS_TO_SHOW) {
                    sortedLabels.poll()
                }
            }
        }

        var textToShow = ""
        val size = sortedLabels.size
        repeat(size) {
            val label = sortedLabels.poll()
            textToShow = label.key
        }
        return textToShow
    }

    /** Reads label list from Assets. */
    @Throws(IOException::class)
    private fun loadLabelList(activity: Activity): List<String> {
        val labelList = mutableListOf<String>()
        BufferedReader(InputStreamReader(activity.assets.open(LABEL_PATH))).use { reader ->
            reader.forEachLine { labelList.add(it) }
        }
        return labelList
    }

    /** Load the model file from the assets folder */
    @Throws(IOException::class)
    private fun loadModelFile(activity: Activity): MappedByteBuffer {
        val fileDescriptor: AssetFileDescriptor = activity.assets.openFd(MODEL_PATH)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel: FileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    /** Converts bitmap into the Byte Buffer to feed into the model */
    private fun preprocess(bitmap: Bitmap?) {
        if (bitmap == null || inputBuffer == null) return

        inputBuffer?.rewind()

        val width = bitmap.width
    }
}

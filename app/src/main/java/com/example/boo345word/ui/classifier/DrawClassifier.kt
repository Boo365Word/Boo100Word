package com.example.boo345word.ui.classifier

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class DrawClassifier(activity: Activity) {
    private val TAG = this.javaClass.simpleName

    // 모델 로드에 필요한 인터프리터 null로 초기화
    private var tflite: Interpreter? = null

    // 모델이 인식하는 카테고리 배열로 선언
    private val classNames = arrayOf("aircraft carrier", "airplane", "alarm clock", "ambulance", "angel",
        "animal migration", "ant", "anvil", "apple", "arm", "asparagus", "axe",
        "backpack", "banana", "bandage", "barn", "baseball bat", "baseball",
        "basket", "basketball", "bat", "bathtub", "beach", "bear", "beard",
        "bed", "bee", "belt", "bench", "bicycle", "binoculars", "bird",
        "birthday cake", "blackberry", "blueberry", "book", "boomerang",
        "bottlecap", "bowtie", "bracelet", "brain", "bread", "bridge",
        "broccoli", "broom", "bucket", "bulldozer", "bus", "bush",
        "butterfly")

    // 모델 경로 선언 (assets 폴더에 있는 모델 파일 이름)
    private val MODEL_PATH = "model.tflite"


    // 초기화 한 인터프리터 객체 생성 (모델 불러오기)
    init {
        try {
            tflite = Interpreter(loadModelFile(activity))
            Log.d(TAG, "Created a TensorFlow Lite MNIST Classifier.")
        } catch (e: IOException) {
            Log.e(TAG, "IOException loading the tflite file", e)
        }
    }

    // 모델 파일 로드
    @Throws(IOException::class)
    private fun loadModelFile(activity: Activity): MappedByteBuffer {

        // MODEL_PATH 경로에 있는 모델 파일 정보를 메모리에 매핑하기 위한 객체
        val fileDescriptor = activity.assets.openFd(MODEL_PATH)

        // 파일 채널을 열어 모델 파일에 접근하기 위한 객체
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)

        // 파일 채널 객체에서 생성한 채널
        val fileChannel = inputStream.channel

        // 모델 파일의 시작 위치 저장, 모델이 저장된 위치 지정 시 필요
        val startOffset = fileDescriptor.startOffset

        // 모델 파일의 전체 길이 저장, 메모리에 매핑할 때 필요한 크기
        val declaredLength = fileDescriptor.declaredLength

        // 모델의 위치와 크기를 사용해서 읽기 전용으로 모델 파일을 메모리에 매핑
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    // 전처리하여 모델에 예측 수행
    fun classify(bitmap: Bitmap, strokes: List<List<Pair<Float, Float>>>): String {
        if (tflite == null) {
            Log.e(TAG, "Image classifier has not been initialized; Skipped.")
            return ""
        }

        val bitmapInput = preprocessBitmapTo64x64(bitmap)
        val strokeInput = preprocessStrokes(strokes)

        val outputBuffer = Array(1) { FloatArray(classNames.size) }
        tflite?.runForMultipleInputsOutputs(arrayOf(bitmapInput, strokeInput), mapOf(0 to outputBuffer))

        val top3Predictions = outputBuffer[0].mapIndexed { index, probability ->
            index to probability
        }.sortedByDescending { it.second }
            .take(3)
            .map { (index, probability) ->
                val category = classNames.getOrNull(index) ?: "Unknown"
                "$category: ${String.format("%.2f", probability * 100)}%"
            }
            .joinToString("\n")

        return top3Predictions
    }

    // 그림을 모델 입력 형식으로 전처리
    private fun preprocessStrokes(strokes: List<List<Pair<Float, Float>>>, maxStrokes: Int = 15, maxLen: Int = 100): Array<Array<Array<FloatArray>>> {
        val input = Array(1) { Array(maxStrokes) { Array(maxLen) { FloatArray(2) } } }

        val allX = strokes.flatMap { stroke -> stroke.map { it.first } }
        val allY = strokes.flatMap { stroke -> stroke.map { it.second } }
        val minX = allX.minOrNull() ?: 0f
        val maxX = allX.maxOrNull() ?: 1f
        val minY = allY.minOrNull() ?: 0f
        val maxY = allY.maxOrNull() ?: 1f
        val width = maxX - minX
        val height = maxY - minY

        for (i in strokes.indices) {
            if (i >= maxStrokes) break
            val stroke = strokes[i]
            for (j in stroke.indices) {
                if (j >= maxLen) break
                input[0][i][j][0] = (stroke[j].first - minX) / (width.takeIf { it != 0f } ?: 1f)
                input[0][i][j][1] = (stroke[j].second - minY) / (height.takeIf { it != 0f } ?: 1f)
            }
        }
        return input
    }

    private fun preprocessBitmapTo64x64(bitmap: Bitmap): Array<Array<Array<FloatArray>>> {
        // 64x64로 바로 리사이즈
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 64, 64, true)

        // 전처리된 배열 생성
        val inputArray = Array(1) { Array(64) { Array(64) { FloatArray(1) } } }

        // 그레이스케일 및 정규화
        for (y in 0 until 64) {
            for (x in 0 until 64) {
                val pixel = resizedBitmap.getPixel(x, y)
                val grayscale = 0.299 * Color.red(pixel) + 0.587 * Color.green(pixel) + 0.114 * Color.blue(pixel)
                inputArray[0][y][x][0] = (grayscale / 255.0).toFloat()  // [0, 1]로 정규화
            }
        }

        return inputArray
    }
}
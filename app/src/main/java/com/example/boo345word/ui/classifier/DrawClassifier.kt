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
    private var tflite: Interpreter? = null
    private val classNames = arrayOf("aircraft_carrier", "airplane", "alarm_clock", "ambulance", "angel",
        "animal_migration", "ant", "anvil", "apple", "arm", "asparagus", "axe", "backpack", "banana", "bandage")
    private val MODEL_PATH = "quick_draw_model.tflite"


    init {
        try {
            tflite = Interpreter(loadModelFile(activity))
            Log.d(TAG, "Created a TensorFlow Lite MNIST Classifier.")
        } catch (e: IOException) {
            Log.e(TAG, "IOException loading the tflite file", e)
        }
    }

    fun classify(bitmap: Bitmap): String {
        if (tflite == null) {
            Log.e(TAG, "Image classifier has not been initialized; Skipped.")
            return ""
        }

        return predict(bitmap)
    }

    // 모델 파일 로드
    @Throws(IOException::class)
    private fun loadModelFile(activity: Activity): MappedByteBuffer {
        val fileDescriptor = activity.assets.openFd(MODEL_PATH)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    // 전처리하여 모델에 예측 수행
    private fun predict(bitmap: Bitmap): String {
        // 1. 그림판의 비트맵을 전처리 (28x28 크기, 그레이스케일, 0~1 정규화)
        val inputArray = preprocessImageForModel(bitmap)

        // 2. 모델 예측 수행
        val output = Array(1) { FloatArray(classNames.size) }
        tflite?.run(inputArray, output)

        // 3. 예측 확률이 가장 높은 클래스 및 확률 값 추출
        val maxIndex = output[0].indices.maxByOrNull { output[0][it] } ?: -1
        val confidence = if (maxIndex >= 0) output[0][maxIndex] * 100 else 0f  // 확률을 퍼센트로 변환

        // 4. 결과 표시
        return if (maxIndex >= 0) {
            "Prediction: ${classNames[maxIndex]}, Confidence: ${"%.2f".format(confidence)}%"
        } else {
            "Unknown"
        }
    }

    // 그림을 모델 입력 형식으로 전처리
    private fun preprocessImageForModel(bitmap: Bitmap): Array<Array<Array<FloatArray>>> {
        // 이진화 임계값 설정
        val thresholdHigh = 0.05f
        val thresholdLow = 0.00f

        // 고해상도에서 28*28까지 축소
        val highResBitmap0 = Bitmap.createScaledBitmap(bitmap, 896, 896, true)
        val highResBitmap1 = Bitmap.createScaledBitmap(highResBitmap0, 448, 448, true)
        val highResBitmap2 = Bitmap.createScaledBitmap(highResBitmap1, 224, 224, true)
        val highResBitmap3 = Bitmap.createScaledBitmap(highResBitmap2, 112, 112, true)
        val highResBitmap4 = Bitmap.createScaledBitmap(highResBitmap3, 56, 56, true)
        val resizedBitmap = Bitmap.createScaledBitmap(highResBitmap4, 28, 28, true)

        // 0~1로 정규화된 그레이스케일 float 배열 생성, 배경-그림 색상 반전
        val inputArray = Array(1) { Array(28) { Array(28) { FloatArray(1) } } }
        for (i in 0 until 28) {
            for (j in 0 until 28) {
                // 각 픽셀 색상 가져오기
                val pixel = resizedBitmap.getPixel(j, i)

                // RGB -> 그레이스케일 변환 및 반전 (1.0 - grayscale)
                val grayscale = 1.0f - ((Color.red(pixel) * 0.299f +
                        Color.green(pixel) * 0.587f +
                        Color.blue(pixel) * 0.114f) / 255.0f)

                // 다중 임계값에 따른 값 설정
                val transformedValue = when {
                    grayscale > thresholdHigh -> 1.0f  // 흰색
                    grayscale > thresholdLow -> 0.7f   // 회색
                    else -> 0.0f                       // 검은색
                }
                inputArray[0][i][j][0] = transformedValue
            }
        }

        return inputArray
    }
}
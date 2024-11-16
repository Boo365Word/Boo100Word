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

class DrawClassifier(
    activity: Activity,
) {
    @Suppress("ktlint:standard:property-naming")
    private val TAG = this.javaClass.simpleName

    // 모델 로드에 필요한 인터프리터 null로 초기화
    private var tflite: Interpreter? = null

    // 모델이 인식하는 카테고리 배열로 선언
    private val classNames =
        arrayOf(
            "aircraft_carrier",
            "airplane",
            "alarm_clock",
            "ambulance",
            "angel",
            "animal_migration",
            "ant",
            "anvil",
            "apple",
            "arm",
            "asparagus",
            "axe",
            "backpack",
            "banana",
            "bandage",
        )

    @Suppress("ktlint:standard:property-naming")
    // 모델 경로 선언 (assets 폴더에 있는 모델 파일 이름)
    private val MODEL_PATH = "quick_draw_model.tflite"

    // 초기화 한 인터프리터 객체 생성 (모델 불러오기)
    init {
        try {
            tflite = Interpreter(loadModelFile(activity))
            Log.d(TAG, "Created a TensorFlow Lite MNIST Classifier.")
        } catch (e: IOException) {
            Log.e(TAG, "IOException loading the tflite file", e)
        }
    }

    // 예측할 타겟 단어를 넘겨준다.
    fun classify(
        bitmap: Bitmap,
        targetWord: String,
    ): Boolean {
        if (tflite == null) {
            Log.e(TAG, "Image classifier has not been initialized; Skipped.")
            return false
        }
        return predict(bitmap, targetWord)
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
    private fun predict(
        bitmap: Bitmap,
        targetWord: String,
    ): Boolean {
        // 1. 그림판의 비트맵을 전처리 (28x28 크기, 그레이스케일, 0~1 정규화)
        val inputArray = preprocessImageForModel(bitmap)

        // 2. 모델 예측 수행
        val output = Array(1) { FloatArray(classNames.size) }
        tflite?.run(inputArray, output)

        // 3. 예측 확률이 가장 높은 클래스 및 확률 값 추출
        val maxIndex = output[0].indices.maxByOrNull { output[0][it] } ?: -1
        val confidence = if (maxIndex >= 0) output[0][maxIndex] * 100 else 0f // 확률을 퍼센트로 변환

        // 4. 모델이 예측한 단어
        val predictedWord = if (maxIndex >= 0) classNames[maxIndex] else "Unknown"

        // 만약 모델이 예측한 단어와 게임에 제시된 단어가 동일하다면 정답 처리
        val result = predictedWord == targetWord

        return result
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
                val grayscale =
                    1.0f - (
                        (
                            Color.red(pixel) * 0.299f +
                                Color.green(pixel) * 0.587f +
                                Color.blue(pixel) * 0.114f
                        ) / 255.0f
                    )

                // 다중 임계값에 따른 값 설정
                val transformedValue =
                    when {
                        grayscale > thresholdHigh -> 1.0f // 흰색
                        grayscale > thresholdLow -> 0.7f // 회색
                        else -> 0.0f // 검은색
                    }
                inputArray[0][i][j][0] = transformedValue
            }
        }

        return inputArray
    }
}

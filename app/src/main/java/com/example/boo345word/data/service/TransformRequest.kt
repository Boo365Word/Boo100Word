package com.example.boo345word.data.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class TransformRequest(
    val strokes: List<List<List<Int>>>, // Assuming List of List of List of Integers for strokes
    val box: List<Int> // Bounding box [minX, minY, maxX, maxY]
)

interface ApiService {
    @POST("/transform")
    suspend fun transformStrokes(@Body request: TransformRequest): Response<ResponseBody>
}

val retrofit = Retrofit.Builder()
    .baseUrl("https://quickdraw-cnn.fly.dev/") // Replace with your server URL
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val apiService = retrofit.create(ApiService::class.java)
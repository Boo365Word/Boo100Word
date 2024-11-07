package com.example.boo345word.data.repository

import kotlinx.coroutines.flow.Flow

interface WordListRepository {

    suspend fun fetchWords(keyword: String): Flow<List<String>>
}
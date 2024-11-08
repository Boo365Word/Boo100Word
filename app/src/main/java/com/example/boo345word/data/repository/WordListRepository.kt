package com.example.boo345word.data.repository

import com.example.boo345word.ui.detail.WordDetail
import kotlinx.coroutines.flow.Flow

interface WordListRepository {

    suspend fun fetchWords(keyword: String): Flow<List<String>>

    suspend fun fetchWordDetail(english: String): Flow<WordDetail>
}
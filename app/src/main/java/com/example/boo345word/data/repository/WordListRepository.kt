package com.example.boo345word.data.repository

import com.example.boo345word.ui.detail.WordDetail
import com.example.boo345word.ui.word.Word
import kotlinx.coroutines.flow.Flow

interface WordListRepository {

    suspend fun fetchWords(keyword: String): Flow<List<Word>>

    suspend fun fetchWordDetail(english: String): Flow<WordDetail>
}

package com.example.boo345word.data.repository

import com.example.boo345word.data.entity.BasicWord
import com.example.boo345word.ui.detail.WordDetail
import com.example.boo345word.ui.word.Word
import kotlinx.coroutines.flow.Flow

interface WordRepository {

    // 학습되지 않은 5개의 단어 랜덤으로 가져오기위해서는 메서드로 정의해줘야함
    suspend fun getFiveBasicWords(): Flow<List<BasicWord>>

    // 학습 상태 업데이트
    suspend fun updateStatus(word: String)

    suspend fun getAllWords(): Flow<List<Word>>

    suspend fun getWordsByKeyword(keyword: String): Flow<List<Word>>

    suspend fun getWordDetailByKeyword(keyword: String): Flow<WordDetail>
}

package com.example.boo345word.data.repository

import com.example.boo345word.data.entity.BasicWord
import kotlinx.coroutines.flow.Flow

interface WordRepository  {
    // 학습되지 않은 5개의 단어 랜덤으로 가져오기위해서는 메서드로 정의해줘야함
    suspend fun getFiveBasicWords(): Flow<List<BasicWord>>

    // 정답 완료로 업데이트
    suspend fun markWordAsLearned(word: String)
}

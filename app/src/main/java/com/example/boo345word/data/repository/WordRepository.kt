package com.example.boo345word.data.repository

import com.example.boo345word.data.dao.WordDao
import com.example.boo345word.data.model.BasicWord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class WordRepository(
    private val wordDao: WordDao,
) {
    // 프로퍼티로 선언된 Flow는 싱글톤처럼 동작한다.
    // 이 Flow는 클래스가 초기화될 때 한번만 생성되며
    // 같은 데이터 스트림을 계속 재사용하게 된다.
    // 즉 최초 생성된 Flow 데이터만 계속 방출한다.
//    val basicWordList: Flow<List<BasicWord>> = wordDao.getFiveBasicWords()

    // 학습되지 않은 5개의 단어 랜덤으로 가져오기위해서는 메서드로 정의해줘야함
    fun getFiveBasicWords(): Flow<List<BasicWord>> = wordDao.getFiveBasicWords()

    suspend fun insertWord(list: List<BasicWord>) {
        withContext(Dispatchers.IO) {
            wordDao.insertWords(list)
        }
    }

    // 학습 완료로 업데이트
    suspend fun markWordAsLearned(word: String) {
        withContext(Dispatchers.IO) {
            wordDao.updateLearnedStatus(word)
        }
    }
}

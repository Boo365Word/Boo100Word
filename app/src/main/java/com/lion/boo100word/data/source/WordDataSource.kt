package com.lion.boo100word.data.source

import com.lion.boo100word.data.entity.BasicWord
import com.lion.boo100word.data.entity.DetailWord
import kotlinx.coroutines.flow.Flow

interface WordDataSource {

    suspend fun getFiveBasicWords(): Flow<List<BasicWord>>

    suspend fun markWordAsLearned(word: String)

    suspend fun selectWordsByKeyword(keyword: String): Flow<List<DetailWord>>

    suspend fun selectWordByKeyword(keyword: String): Flow<DetailWord>

    suspend fun getAllWords(): Flow<List<DetailWord>>
}

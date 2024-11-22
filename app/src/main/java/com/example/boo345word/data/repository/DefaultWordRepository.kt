package com.example.boo345word.data.repository

import com.example.boo345word.data.entity.BasicWord
import com.example.boo345word.data.source.WordDataSource
import com.example.boo345word.mapper.DetailWordMapper.toDomain
import com.example.boo345word.ui.detail.WordDetail
import com.example.boo345word.ui.word.Word
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultWordRepository
@Inject
constructor(
    private val dataSource: WordDataSource
) : WordRepository {

    override suspend fun getFiveBasicWords(): Flow<List<BasicWord>> = dataSource.getFiveBasicWords()

    override suspend fun updateStatus(word: String) = dataSource.markWordAsLearned(word)

    override suspend fun getAllWords(): Flow<List<Word>> = dataSource
        .getAllWords()
        .map {
            it.toDomain()
        }

    override suspend fun getWordsByKeyword(keyword: String): Flow<List<Word>> = dataSource
        .selectWordsByKeyword(keyword.replace(' ', '_'))
        .map {
            it.toDomain()
        }

    override suspend fun getWordDetailByKeyword(keyword: String): Flow<WordDetail> = dataSource
        .selectWordByKeyword(keyword.replace(' ', '_'))
        .map {
            it.toDomain()
        }
}

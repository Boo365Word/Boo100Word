package com.example.boo345word.data.repository

import com.example.boo345word.data.entity.BasicWord
import com.example.boo345word.data.source.WordDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WordRepositoryImpl
    @Inject
    constructor(
        private val dataSource: WordDataSource,
    ) : WordRepository {
        override suspend fun getFiveBasicWords(): Flow<List<BasicWord>> = dataSource.getFiveBasicWords()

        override suspend fun updateStatus(word: String) = dataSource.markWordAsLearned(word)
    }

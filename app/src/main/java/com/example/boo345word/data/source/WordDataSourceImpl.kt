package com.example.boo345word.data.source

import com.example.boo345word.data.dao.BasicWordDao
import com.example.boo345word.data.dao.DetailWordDao
import com.example.boo345word.data.entity.BasicWord
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WordDataSourceImpl
    @Inject
    constructor(
        private val basicWordDao: BasicWordDao,
        private val detailWordDao: DetailWordDao,
        private val ioDispatchers: CoroutineDispatcher,
    ) : WordDataSource {
        override suspend fun getFiveBasicWords(): Flow<List<BasicWord>> = withContext(ioDispatchers) { basicWordDao.getFiveBasicWords() }

        override suspend fun markWordAsLearned(word: String) {
            withContext(ioDispatchers) {
                detailWordDao.updateCorrectStatus(word)
            }
        }
    }

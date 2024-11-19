package com.example.boo345word.data.source

import com.example.boo345word.data.dao.BasicWordDao
import com.example.boo345word.data.dao.DetailWordDao
import com.example.boo345word.data.entity.BasicWord
import com.example.boo345word.data.entity.DetailWord
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultWordDataSource
@Inject
constructor(
    private val basicWordDao: BasicWordDao,
    private val detailWordDao: DetailWordDao,
    private val ioDispatchers: CoroutineDispatcher
) : WordDataSource {
    override suspend fun getFiveBasicWords(): Flow<List<BasicWord>> = withContext(ioDispatchers) { basicWordDao.getFiveBasicWords() }

    override suspend fun markWordAsLearned(word: String) {
        withContext(ioDispatchers) {
            detailWordDao.updateCorrectStatus(word)
        }
    }

    override suspend fun selectWordsByKeyword(keyword: String): Flow<List<DetailWord>> = withContext(ioDispatchers) {
        detailWordDao.selectWordsByKeyword(keyword)
    }

    override suspend fun selectWordByKeyword(keyword: String): Flow<DetailWord> = withContext(ioDispatchers) {
        detailWordDao.selectWordByKeyword(keyword)
    }

    override suspend fun getAllWords(): Flow<List<DetailWord>> = withContext(ioDispatchers) {
        detailWordDao.selectWords()
    }
}

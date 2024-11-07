package com.example.boo345word.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DefaultWordListRepository : WordListRepository {

    private val words = listOf(
        "apple", "orange", "apple", "orange", "apple", "orange", "apple", "orange",
    )

    override suspend fun fetchWords(keyword: String): Flow<List<String>> = flow {
        if (keyword.isEmpty()) emit(words)

        emit(
            words.filter { word ->
                word.contains(keyword)
            }
        )
    }
}

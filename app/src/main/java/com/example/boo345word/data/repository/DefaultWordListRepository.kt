package com.example.boo345word.data.repository

import com.example.boo345word.ui.detail.Sentence
import com.example.boo345word.ui.detail.WordDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DefaultWordListRepository : WordListRepository {

    private val words = listOf(
        "apple", "orange", "apple", "orange", "apple", "orange", "apple", "orange",
    )
    private val wordsDetail = mapOf(
        "apple" to listOf(Sentence("I ate an apple", "난 사과를 먹었어요")),
        "orange" to listOf(Sentence("I hate orange", "나는 오렌지가 싫어요"))
    )

    override suspend fun fetchWords(keyword: String): Flow<List<String>> = flow {
        if (keyword.isEmpty()) emit(words)

        emit(
            words.filter { word ->
                word.contains(keyword)
            }
        )
    }

    override suspend fun fetchWordDetail(english: String): Flow<WordDetail> = flow {
        emit(
            WordDetail(
                english = english,
                pronunciation = english,
                korean = "한글 뜻",
                sentences = wordsDetail[english] ?: throw NoSuchElementException(
                    NONE_OF_EXAMPLE_SENTENCE
                )
            )
        )
    }

    companion object {

        private const val NONE_OF_EXAMPLE_SENTENCE = "해당 단어에 대한 예문이 존재하지 않습니다."
    }
}

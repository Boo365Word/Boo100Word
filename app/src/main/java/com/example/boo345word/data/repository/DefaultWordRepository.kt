package com.example.boo345word.data.repository

import com.example.boo345word.ui.detail.Sentence
import com.example.boo345word.ui.detail.WordDetail
import com.example.boo345word.ui.word.Word
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DefaultWordRepository : WordRepository {

    private val words = listOf(
        Word(
            english = "apple",
            korean = "사과",
            isCorrect = true
        ),
        Word(
            english = "orange",
            korean = "오렌지",
            isCorrect = false
        ),
        Word(
            english = "peach",
            korean = "복숭아",
            isCorrect = true
        ),
        Word(
            english = "grape",
            korean = "포도",
            isCorrect = false
        ),
        Word(
            english = "watermelon",
            korean = "수박",
            isCorrect = false
        ),
        Word(
            english = "garlic",
            korean = "마늘",
            isCorrect = true
        ),
        Word(
            english = "anchovy",
            korean = "멸치",
            isCorrect = false
        ),
        Word(
            english = "onion",
            korean = "양파",
            isCorrect = true
        )
    )
    private val wordsDetail = mapOf(
        "apple" to listOf(Sentence("I ate an apple", "난 사과를 먹었어요")),
        "orange" to listOf(Sentence("I hate orange", "나는 오렌지가 싫어요"))
    )

    override suspend fun fetchWords(keyword: String): Flow<List<Word>> = flow {
        if (keyword.isEmpty()) emit(words)

        emit(
            words.filter { word -> word.english.contains(keyword) || word.korean.contains(keyword)}
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

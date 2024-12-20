package com.lion.boo100word.mapper

import com.lion.boo100word.data.entity.DetailWord
import com.lion.boo100word.ui.detail.Sentence
import com.lion.boo100word.ui.detail.WordDetail
import com.lion.boo100word.ui.word.Word

object DetailWordMapper {

    fun List<DetailWord>.toDomain(): List<Word> = this.map {
        Word(
            english = it.word.replace('_', ' '),
            korean = it.meaning,
            isCorrect = when (it.status) {
                0, 2 -> false
                else -> true
            }
        )
    }

    fun DetailWord.toDomain(): WordDetail = WordDetail(
        english = word.replace('_', ' '),
        korean = meaning,
        pronunciation = pronunciation,
        sentences = listOf(
            Sentence(
                english = example1,
                korean = example1Meaning
            ),
            Sentence(
                english = example2,
                korean = example2Meaning
            )
        )
    )
}

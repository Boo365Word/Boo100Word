package com.lion.boo100word.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detail_words")
// 단어장에서 사용될 단어의 상세 정보
data class DetailWord(
    @PrimaryKey val word: String,
    val meaning: String,
    val pronunciation: String,
    val example1: String,
    val example1Meaning: String,
    val example2: String,
    val example2Meaning: String,
    val status: Int = 0
    /*
    0 -> 학습 x
    1 -> 정답
    2 -> 오답
     */
)

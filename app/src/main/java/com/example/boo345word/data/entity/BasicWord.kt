package com.example.boo345word.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// 게임에 사용될 단어의 기본 정보
@Entity(tableName = "basic_words")
data class BasicWord(
    @PrimaryKey val word: String,
    val meaning: String
)

package com.example.boo345word.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// 단어 상세
// @Entity(
//    tableName = "detail_words",
//    foreignKeys = [
//        ForeignKey(
//            entity = BasicWord::class,
//            parentColumns = ["word"],
//            childColumns = ["words"],
//            onDelete = ForeignKey.CASCADE,
//        ),
//    ],
// )

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
    val isLearned: Boolean = false,
)

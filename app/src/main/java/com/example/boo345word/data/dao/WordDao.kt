package com.example.boo345word.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.boo345word.data.model.BasicWord
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<BasicWord>)

//    @Query("Select word , meaning from words")
//    fun getWord(): List<BasicWord>

    // 학습되지 않은 단어 중 랜덤으로 가져오기 where isLearned = true
    @Query("Select * from basic_words order by random() limit 5")
    fun getFiveBasicWords(): Flow<List<BasicWord>>

    // 학습 여부 업데이트
    @Query("Update basic_words set isLearned = true where word = :word")
    suspend fun updateLearnedStatus(word: String)
}

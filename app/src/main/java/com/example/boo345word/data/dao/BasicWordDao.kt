package com.example.boo345word.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.boo345word.data.entity.BasicWord
import kotlinx.coroutines.flow.Flow

@Dao
interface BasicWordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBasicWords(words: List<BasicWord>)

    // 학습되지 않은 단어 중 랜덤으로 가져오기 where isLearned = true
    @Query("Select * from basic_words order by random() limit 5")
    fun getFiveBasicWords(): Flow<List<BasicWord>>
}

package com.example.boo345word.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.boo345word.data.entity.DetailWord

@Dao
interface DetailWordDao {
    // 정답 여부 업데이트
    @Query("Update detail_words set status = 1 where word = :word")
    suspend fun updateCorrectStatus(word: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetailWords(words: List<DetailWord>)
}

package com.lion.boo100word.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lion.boo100word.data.dao.BasicWordDao
import com.lion.boo100word.data.dao.DetailWordDao
import com.lion.boo100word.data.entity.BasicWord
import com.lion.boo100word.data.entity.DetailWord

@Database(entities = [BasicWord::class, DetailWord::class], version = 1)
abstract class WordDatabase : RoomDatabase() {

    abstract fun getBasicWordDao(): BasicWordDao

    abstract fun getDetailWordDao(): DetailWordDao
}

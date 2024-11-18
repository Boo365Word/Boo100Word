package com.example.boo345word.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.boo345word.data.dao.BasicWordDao
import com.example.boo345word.data.dao.DetailWordDao
import com.example.boo345word.data.entity.BasicWord
import com.example.boo345word.data.entity.DetailWord

@Database(entities = [BasicWord::class, DetailWord::class], version = 1)
abstract class WordDatabase : RoomDatabase() {

    abstract fun getBasicWordDao(): BasicWordDao

    abstract fun getDetailWordDao(): DetailWordDao
}

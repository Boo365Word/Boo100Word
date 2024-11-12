package com.example.boo345word.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.boo345word.data.dao.WordDao
import com.example.boo345word.data.model.BasicWord
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = arrayOf(BasicWord::class), version = 1)
abstract class WordDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao

    companion object {
        @Suppress("ktlint:standard:property-naming")
        @Volatile
        private var INSTANCE: WordDatabase? = null

        fun getDatabase(context: Context): WordDatabase =
            INSTANCE ?: synchronized(this) {
                val instance =
                    Room
                        .databaseBuilder(
                            context.applicationContext,
                            WordDatabase::class.java,
                            "word_database",
                        ).addCallback(PrepopulateCallBack(context))
                        .build()
                INSTANCE = instance
                instance
            }
    }

    private class PrepopulateCallBack(
        private val context: Context,
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE.let {
                CoroutineScope(Dispatchers.IO).launch {
                    insertWord(context, it!!.wordDao())
                }
            }
        }

        private suspend fun insertWord(
            context: Context,
            wordDao: WordDao,
        ) {
            val wordList = getWords(context = context)
            wordDao.insertWords(wordList)
        }

        // json 파일 읽기
        private fun getWords(context: Context): List<BasicWord> {
            val inputStream = context.assets.open("class_names.json")
            val json = inputStream.bufferedReader().use { it.readText() }
            val gson = Gson()
            val listType =
                object : TypeToken<List<BasicWord>>() {}.type
            return gson.fromJson(json, listType)
        }
    }
}

package com.example.boo345word.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.boo345word.data.dao.BasicWordDao
import com.example.boo345word.data.dao.DetailWordDao
import com.example.boo345word.data.entity.BasicWord
import com.example.boo345word.data.entity.DetailWord
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [BasicWord::class, DetailWord::class], version = 1)
abstract class WordDatabase : RoomDatabase() {
    abstract fun getBasicWordDao(): BasicWordDao

    abstract fun getDetailWordDao(): DetailWordDao

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

    class PrepopulateCallBack(
        private val context: Context,
        private val ioDispatchers: CoroutineDispatcher = Dispatchers.IO,
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE.let {
                CoroutineScope(ioDispatchers).launch {
                    insertBasicWord(context, it!!.getBasicWordDao())
                    insertDetailWord(context, it.getDetailWordDao())
                }
            }
        }

        private suspend fun insertBasicWord(
            context: Context,
            basicWordDao: BasicWordDao,
        ) {
            val wordList = getBasicWords(context = context)
            basicWordDao.insertBasicWords(wordList)
        }

        private suspend fun insertDetailWord(
            context: Context,
            detailWordDao: DetailWordDao,
        ) {
            val wordList = getDetailWords(context = context)
            detailWordDao.insertDetailWords(wordList)
        }

        // basic words json 파일 읽기
        private fun getBasicWords(context: Context): List<BasicWord> {
            val inputStream = context.assets.open("basic_words.json")
            val json = inputStream.bufferedReader().use { it.readText() }
            val gson = Gson()
            val listType =
                object : TypeToken<List<BasicWord>>() {}.type
            return gson.fromJson(json, listType)
        }

        private fun getDetailWords(context: Context): List<DetailWord> {
            val inputStream = context.assets.open("detail_words.json")
            val json = inputStream.bufferedReader().use { it.readText() }
            val gson = Gson()
            val listType =
                object : TypeToken<List<DetailWord>>() {}.type
            return gson.fromJson(json, listType)
        }
    }
}

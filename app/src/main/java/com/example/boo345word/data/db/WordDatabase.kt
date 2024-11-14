package com.example.boo345word.data.db

import android.content.Context
import android.util.Log
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
import kotlinx.coroutines.launch

@Database(entities = [BasicWord::class, DetailWord::class], version = 1)
abstract class WordDatabase : RoomDatabase() {
    abstract fun getBasicWordDao(): BasicWordDao

    abstract fun getDetailWordDao(): DetailWordDao

    class PrepopulateCallBack(
        private val context: Context,
        private val ioDispatchers: CoroutineDispatcher,
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            // 데이터 베이스 최초 생성시에만 호출된다.
            super.onCreate(db)
            CoroutineScope(ioDispatchers).launch {
                try {
                    val database =
                        Room
                            .databaseBuilder(
                                context,
                                WordDatabase::class.java,
                                "word_database",
                            ).build()

                    insertBasicWord(context, database.getBasicWordDao())
                    insertDetailWord(context, database.getDetailWordDao())
                    Log.d("WordDatabase", "Initial data successfully inserted.")
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("WordDatabase", "데이터 초기화 중 오류 발생: ${e.message}")
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

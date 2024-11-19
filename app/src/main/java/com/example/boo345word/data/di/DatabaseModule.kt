package com.example.boo345word.data.di

import android.content.Context
import androidx.room.Room
import com.example.boo345word.data.db.WordDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): WordDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            WordDatabase::class.java,
            "word_database"
        ).createFromAsset("words.db")
        .build()

    @Provides
    @Singleton
    fun provideBasicWordDao(database: WordDatabase) = database.getBasicWordDao()

    @Provides
    @Singleton
    fun provideDetailWordDao(database: WordDatabase) = database.getDetailWordDao()

    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}

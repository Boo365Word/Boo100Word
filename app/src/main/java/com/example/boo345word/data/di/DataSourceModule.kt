package com.example.boo345word.data.di

import com.example.boo345word.data.source.WordDataSource
import com.example.boo345word.data.source.WordDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {
    @Binds
    fun provideWordDataSourceImpl(dataSourceImpl: WordDataSourceImpl): WordDataSource
}

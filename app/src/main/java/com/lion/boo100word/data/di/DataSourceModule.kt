package com.lion.boo100word.data.di

import com.lion.boo100word.data.source.DefaultWordDataSource
import com.lion.boo100word.data.source.WordDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    fun provideWordDataSourceImpl(dataSourceImpl: DefaultWordDataSource): WordDataSource
}

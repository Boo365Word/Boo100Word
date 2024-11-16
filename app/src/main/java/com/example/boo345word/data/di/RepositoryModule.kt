package com.example.boo345word.data.di

import com.example.boo345word.data.repository.WordRepository
import com.example.boo345word.data.repository.WordRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {
    @Binds
    fun provideWordRepositoryImpl(repositoryImpl: WordRepositoryImpl): WordRepository
}

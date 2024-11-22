package com.example.boo345word.data.di

import com.example.boo345word.data.repository.DefaultWordRepository
import com.example.boo345word.data.repository.WordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun provideWordRepositoryImpl(repositoryImpl: DefaultWordRepository): WordRepository
}

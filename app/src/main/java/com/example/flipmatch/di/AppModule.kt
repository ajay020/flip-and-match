package com.example.flipmatch.di

import android.content.Context
import com.example.flipmatch.data.repository.PuzzleRepository
import com.example.flipmatch.data.repository.PuzzleRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePuzzleRepository(
        @ApplicationContext context: Context,
    ): PuzzleRepository = PuzzleRepositoryImpl(context)
}

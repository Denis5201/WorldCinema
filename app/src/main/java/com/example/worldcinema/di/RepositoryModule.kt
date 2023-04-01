package com.example.worldcinema.di

import com.example.worldcinema.data.repository.AuthRepositoryImpl
import com.example.worldcinema.data.repository.MovieRepositoryImpl
import com.example.worldcinema.data.repository.ProfileRepositoryImpl
import com.example.worldcinema.data.repository.SharedPreferencesRepositoryImpl
import com.example.worldcinema.domain.repository.AuthRepository
import com.example.worldcinema.domain.repository.MovieRepository
import com.example.worldcinema.domain.repository.ProfileRepository
import com.example.worldcinema.domain.repository.SharedPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindSharedPreferencesRepository(
        sharedPreferencesRepositoryImpl: SharedPreferencesRepositoryImpl
    ): SharedPreferencesRepository

    @Binds
    @Singleton
    abstract fun bindMovieRepository(movieRepositoryImpl: MovieRepositoryImpl): MovieRepository
}
package com.example.worldcinema.di

import com.example.worldcinema.data.repository.*
import com.example.worldcinema.domain.repository.*
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

    @Binds
    @Singleton
    abstract fun bindEpisodeTimeRepository(
        episodeTimeRepositoryImpl: EpisodeTimeRepositoryImpl
    ): EpisodeTimeRepository

    @Binds
    @Singleton
    abstract fun bindCollectionRepository(
        collectionRepositoryImpl: CollectionRepositoryImpl
    ): CollectionRepository
}
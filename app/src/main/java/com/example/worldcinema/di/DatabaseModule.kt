package com.example.worldcinema.di

import android.content.Context
import androidx.room.Room
import com.example.worldcinema.data.AppDatabase
import com.example.worldcinema.data.dao.CollectionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext applicationContext: Context): AppDatabase {
        return Room.databaseBuilder(applicationContext, AppDatabase::class.java, DATABASE_NAME)
            .build()
    }

    @Provides
    @Singleton
    fun provideCollectionDao(db: AppDatabase): CollectionDao = db.collectionDao()

    private const val DATABASE_NAME = "collectionDB"
}
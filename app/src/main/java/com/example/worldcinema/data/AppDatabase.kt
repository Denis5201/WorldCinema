package com.example.worldcinema.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.worldcinema.data.dao.CollectionDao
import com.example.worldcinema.data.entity.CollectionEntity

@Database(entities = [CollectionEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun collectionDao(): CollectionDao
}
package com.example.worldcinema.data.dao

import androidx.room.*
import com.example.worldcinema.data.entity.CollectionEntity

@Dao
interface CollectionDao {

    @Insert
    suspend fun addCollection(collectionEntity: CollectionEntity)

    @Query("SELECT * FROM collection WHERE userId = :userId")
    fun getUserCollectionList(userId: String): List<CollectionEntity>

    @Upsert
    suspend fun upsertCollection(collectionEntity: CollectionEntity)

    @Query("DELETE FROM collection WHERE collectionId = :collectionId")
    suspend fun deleteCollection(collectionId: String)
}
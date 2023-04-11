package com.example.worldcinema.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collection")
data class CollectionEntity(
    @PrimaryKey val collectionId: String,
    val userId: String,
    val name: String,
    val image: String?
)

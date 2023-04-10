package com.example.worldcinema.data.dto

import com.example.worldcinema.domain.model.CollectionInfo
import kotlinx.serialization.Serializable

@Serializable
data class CollectionListItemDto(
    val collectionId: String,
    val name: String
) {
    fun toCollectionInfo(): CollectionInfo {
        return CollectionInfo(
            collectionId = collectionId,
            name = name
        )
    }
}

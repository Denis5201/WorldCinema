package com.example.worldcinema.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CollectionFormDto(
    val name: String
) {
    companion object {
        fun fromString(collectionName: String): CollectionFormDto {
            return CollectionFormDto(
                name = collectionName
            )
        }
    }
}

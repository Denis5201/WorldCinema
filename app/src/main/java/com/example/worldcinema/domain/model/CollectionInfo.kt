package com.example.worldcinema.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CollectionInfo(
    val collectionId: String,
    val name: String,
    val image: String? = null
)

package com.example.worldcinema.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CollectionInfo(
    val collectionId: String,
    var name: String,
    var image: String? = null
)

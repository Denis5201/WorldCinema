package com.example.worldcinema.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Episode(
    val episodeId: String,
    val name: String,
    val description: String,
    val year: Int,
    val runtime: Int,
    val preview: String,
    val filePath: String
)
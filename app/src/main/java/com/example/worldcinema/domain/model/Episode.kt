package com.example.worldcinema.domain.model

data class Episode(
    val episodeId: String,
    val name: String,
    val description: String,
    val year: String,
    val runtime: Int,
    val preview: String,
    val filePath: String
)
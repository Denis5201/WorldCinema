package com.example.worldcinema.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val movieId: String,
    val name: String,
    val description: String,
    val age: String,
    val chatInfo: Chat?,
    val imageUrls: List<String>,
    val poster: String,
    val tags: List<Tag>
)
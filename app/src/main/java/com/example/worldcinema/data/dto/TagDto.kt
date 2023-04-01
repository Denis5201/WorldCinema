package com.example.worldcinema.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TagDto(
    val tagId: String,
    val tagName: String,
    val categoryName: String
)
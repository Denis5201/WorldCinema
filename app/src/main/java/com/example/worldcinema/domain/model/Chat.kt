package com.example.worldcinema.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Chat(
    val chatId: String,
    val name: String
)

package com.example.worldcinema.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatDto(
    val chatId: String,
    val chatName: String,
    val lastMessage: ChatMessageDto?
)

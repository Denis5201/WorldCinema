package com.example.worldcinema.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageDto(
    val messageId: String,
    val creationDateTime: String,
    val authorId: String,
    val authorName: String,
    val authorAvatar: String,
    val text: String
)
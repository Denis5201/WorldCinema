package com.example.worldcinema.domain.model

import java.time.LocalDateTime

data class ChatMessage(
    val messageId: String,
    val creationDateTime: LocalDateTime,
    val authorId: String,
    val authorName: String,
    val authorAvatar: String?,
    val text: String
)

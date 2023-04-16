package com.example.worldcinema.domain.model

data class ChatInfo(
    val chatId: String,
    val chatName: String,
    val lastMessage: ChatMessage?
)

package com.example.worldcinema.data.dto

import com.example.worldcinema.domain.model.ChatInfo
import kotlinx.serialization.Serializable

@Serializable
data class ChatDto(
    val chatId: String,
    val chatName: String,
    val lastMessage: ChatMessageDto?
) {
    fun toChatInfo(): ChatInfo {
        return ChatInfo(
            chatId = chatId,
            chatName = chatName,
            lastMessage = lastMessage?.toChatMessage()
        )
    }
}

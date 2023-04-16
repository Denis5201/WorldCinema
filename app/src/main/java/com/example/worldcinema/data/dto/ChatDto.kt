package com.example.worldcinema.data.dto

import com.example.worldcinema.domain.model.ChatInfo
import com.example.worldcinema.domain.model.ChatMessage
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
            lastMessage = if (lastMessage == null) null else ChatMessage(
                messageId = lastMessage.messageId,
                creationDateTime = lastMessage.creationDateTime,
                authorId = lastMessage.authorId,
                authorName = lastMessage.authorName,
                authorAvatar = lastMessage.authorAvatar,
                text = lastMessage.text
            )
        )
    }
}

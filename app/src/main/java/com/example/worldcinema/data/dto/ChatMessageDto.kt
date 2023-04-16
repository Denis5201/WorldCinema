package com.example.worldcinema.data.dto

import com.example.worldcinema.domain.model.ChatMessage
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@Serializable
data class ChatMessageDto(
    val messageId: String,
    val creationDateTime: String,
    val authorId: String,
    val authorName: String,
    val authorAvatar: String?,
    val text: String
) {
    fun toChatMessage(): ChatMessage {
        val zoneId = ZoneId.systemDefault()
        return ChatMessage(
            messageId = messageId,
            creationDateTime = LocalDateTime.parse(creationDateTime)
                .atOffset(ZoneOffset.UTC).atZoneSameInstant(zoneId).toLocalDateTime(),
            authorId = authorId,
            authorName = authorName,
            authorAvatar = authorAvatar,
            text = text
        )
    }
}
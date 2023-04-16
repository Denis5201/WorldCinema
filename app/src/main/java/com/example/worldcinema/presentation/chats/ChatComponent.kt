package com.example.worldcinema.presentation.chats

import com.example.worldcinema.domain.model.ChatMessage
import java.time.LocalDate

sealed class ChatComponent {
    data class MyMessage(val chatMessage: ChatMessage) : ChatComponent()

    data class UsersMessage(val chatMessage: ChatMessage) : ChatComponent()

    data class DateLabel(val date: LocalDate) : ChatComponent()
}

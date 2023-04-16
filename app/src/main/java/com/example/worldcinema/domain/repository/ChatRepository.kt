package com.example.worldcinema.domain.repository

import androidx.lifecycle.LiveData
import com.example.worldcinema.domain.model.ChatInfo
import com.example.worldcinema.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun getUserChatList(): Flow<Result<List<ChatInfo>>>

    fun setConnection(chatId: String): Flow<Result<LiveData<ChatMessage>>>

    fun sendMessage(message: String): Flow<Result<Unit>>

    fun close(): Flow<Result<Unit>>
}
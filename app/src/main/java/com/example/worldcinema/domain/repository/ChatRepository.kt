package com.example.worldcinema.domain.repository

import com.example.worldcinema.domain.model.ChatInfo
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun getUserChatList(): Flow<Result<List<ChatInfo>>>

    fun setConnection()

    fun sendMessage()
}
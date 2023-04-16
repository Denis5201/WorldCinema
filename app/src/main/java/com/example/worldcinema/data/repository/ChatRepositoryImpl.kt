package com.example.worldcinema.data.repository

import android.util.Log
import com.example.worldcinema.data.api.ChatApi
import com.example.worldcinema.domain.model.ChatInfo
import com.example.worldcinema.domain.repository.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatApi
) : ChatRepository {

    override fun getUserChatList(): Flow<Result<List<ChatInfo>>> = flow {
        try {
            val chatList = api.getUserChat().map { it.toChatInfo() }
            emit(Result.success(chatList))
        } catch (e: Exception) {
            Log.e("OPS getUserChatList", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun setConnection() {
        TODO("Not yet implemented")
    }

    override fun sendMessage() {
        TODO("Not yet implemented")
    }
}
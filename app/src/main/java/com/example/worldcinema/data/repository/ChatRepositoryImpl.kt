package com.example.worldcinema.data.repository

import android.util.Log
import com.example.worldcinema.data.AuthInterceptor
import com.example.worldcinema.data.Authenticator
import com.example.worldcinema.data.ChatWebSocketListener
import com.example.worldcinema.data.api.ChatApi
import com.example.worldcinema.domain.model.ChatInfo
import com.example.worldcinema.domain.repository.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatApi,
    private val authInterceptor: AuthInterceptor,
    private val authenticator: Authenticator
) : ChatRepository {

    private lateinit var socket: WebSocket

    override fun getUserChatList(): Flow<Result<List<ChatInfo>>> = flow {
        try {
            val chatList = api.getUserChat().map { it.toChatInfo() }
            emit(Result.success(chatList))
        } catch (e: Exception) {
            Log.e("OPS getUserChatList", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun setConnection(chatId: String) = flow {
        try {
            val client = OkHttpClient.Builder().apply {
                val logLevel = HttpLoggingInterceptor.Level.BODY
                addInterceptor(HttpLoggingInterceptor().setLevel(logLevel))
                addInterceptor(authInterceptor)
                authenticator(authenticator)
            }.build()
            val request = Request.Builder()
                .url("$CHAT_PREFIX$chatId$CHAT_POSTFIX")
                .build()
            val socketListener = ChatWebSocketListener()

            socket = client.newWebSocket(request, socketListener)

            client.dispatcher.executorService.shutdown()
            emit(Result.success(socketListener.message))
        } catch (e: Exception) {
            Log.e("OPS setConnection", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun sendMessage(message: String) = flow {
        try {
            socket.send(message)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            Log.e("OPS sendMessage", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun close() = flow {
        try {
            socket.close(1000, "")
            Log.e("close", "my close")
            emit(Result.success(Unit))
        } catch (e: Exception) {
            Log.e("OPS close", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    private companion object {
        const val CHAT_PREFIX = "ws://107684.web.hosting-russia.ru:8000/api/chats/"
        const val CHAT_POSTFIX = "/messages"
    }
}
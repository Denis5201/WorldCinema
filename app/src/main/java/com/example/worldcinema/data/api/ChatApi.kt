package com.example.worldcinema.data.api

import com.example.worldcinema.data.dto.ChatDto
import retrofit2.http.GET

interface ChatApi {

    @GET("chats")
    suspend fun getUserChat(): List<ChatDto>
}
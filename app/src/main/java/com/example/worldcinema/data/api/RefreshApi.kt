package com.example.worldcinema.data.api

import com.example.worldcinema.data.dto.TokenDto
import retrofit2.http.Header
import retrofit2.http.POST

interface RefreshApi {
    @POST("auth/refresh")
    suspend fun refresh(@Header("Authorization") refreshToken: String): TokenDto
}
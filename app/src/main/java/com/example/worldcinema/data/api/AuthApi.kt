package com.example.worldcinema.data.api

import com.example.worldcinema.data.dto.CredentialsDto
import com.example.worldcinema.data.dto.RefreshTokenDto
import com.example.worldcinema.data.dto.RegistrationBodyDto
import com.example.worldcinema.data.dto.TokenDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/register")
    suspend fun register(@Body body: RegistrationBodyDto): TokenDto

    @POST("auth/login")
    suspend fun login(@Body body: CredentialsDto): TokenDto

    @POST("auth/refresh")
    suspend fun refresh(@Body body: RefreshTokenDto): TokenDto
}
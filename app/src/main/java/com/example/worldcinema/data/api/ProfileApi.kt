package com.example.worldcinema.data.api

import com.example.worldcinema.data.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProfileApi {

    @GET("profile")
    suspend fun getUserAccount(): UserDto

    @POST("profile/avatar")
    suspend fun loadAvatar(@Body body: String)
}
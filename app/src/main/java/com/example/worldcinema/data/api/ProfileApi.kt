package com.example.worldcinema.data.api

import com.example.worldcinema.data.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ProfileApi {

    @GET("profile")
    suspend fun getUserAccount(): UserDto

    @Multipart
    @POST("profile/avatar")
    suspend fun loadAvatar(@Part("file") file: String)
}
package com.example.worldcinema.data.api

import com.example.worldcinema.data.dto.EpisodeTimeDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EpisodeApi {

    @GET("episodes/{episodeId}/time")
    suspend fun getTimePosition(@Path("episodeId") episodeId: String): EpisodeTimeDto

    @POST("episodes/{episodeId}/time")
    suspend fun saveTimePosition(@Path("episodeId") episodeId: String, @Body body: EpisodeTimeDto)
}
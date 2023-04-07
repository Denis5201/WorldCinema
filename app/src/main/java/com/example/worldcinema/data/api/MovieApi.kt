package com.example.worldcinema.data.api

import com.example.worldcinema.data.dto.CoverMovieDto
import com.example.worldcinema.data.dto.EpisodeDto
import com.example.worldcinema.data.dto.EpisodeViewDto
import com.example.worldcinema.data.dto.MovieDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("cover")
    suspend fun getCover(): CoverMovieDto

    @GET("movies")
    suspend fun getListMovie(@Query("filter") filter: String): List<MovieDto>

    @GET("movies/{movieId}/episodes")
    suspend fun getListEpisode(@Path("movieId") movieId: String): List<EpisodeDto>

    @GET("history")
    suspend fun getHistory(): List<EpisodeViewDto>

    @POST("movies/{movieId}/dislike")
    suspend fun dislike(@Path("movieId") movieId: String)
}
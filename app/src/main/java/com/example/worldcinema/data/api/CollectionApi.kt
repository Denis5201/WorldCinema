package com.example.worldcinema.data.api

import com.example.worldcinema.data.dto.CollectionFormDto
import com.example.worldcinema.data.dto.CollectionListItemDto
import com.example.worldcinema.data.dto.MovieDto
import com.example.worldcinema.data.dto.MovieValueDto
import retrofit2.http.*

interface CollectionApi {

    @GET("collections")
    suspend fun getCollections(): List<CollectionListItemDto>

    @POST("collections")
    suspend fun createCollection(@Body body: CollectionFormDto): CollectionListItemDto

    @DELETE("collections/{collectionId}")
    suspend fun deleteCollection(@Path("collectionId") collectionId: String)

    @GET("collections/{collectionId}/movies")
    suspend fun getCollectionFilms(@Path("collectionId") collectionId: String): List<MovieDto>

    @POST("collections/{collectionId}/movies")
    suspend fun addFilmInCollection(
        @Path("collectionId") collectionId: String,
        @Body body: MovieValueDto
    )

    @DELETE("collections/{collectionId}/movies")
    suspend fun deleteFilmFromCollection(
        @Path("collectionId") collectionId: String,
        @Query("movieId") movieId: String
    )
}
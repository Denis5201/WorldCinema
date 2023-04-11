package com.example.worldcinema.domain.repository

import com.example.worldcinema.domain.model.CollectionInfo
import com.example.worldcinema.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {

    fun getCollections(): Flow<Result<List<CollectionInfo>>>

    fun createCollection(
        nameCollection: String, imageCollection: String?
    ): Flow<Result<CollectionInfo>>

    suspend fun updateCollection(collectionInfo: CollectionInfo)

    fun deleteCollection(collectionId: String): Flow<Result<Unit>>

    fun getCollectionFilms(collectionId: String): Flow<Result<List<Movie>>>

    fun addFilmInCollection(collectionId: String, movieId: String): Flow<Result<Unit>>

    fun deleteFilmFromCollection(collectionId: String, movieId: String): Flow<Result<Unit>>
}
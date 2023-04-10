package com.example.worldcinema.data.repository

import android.util.Log
import com.example.worldcinema.data.api.CollectionApi
import com.example.worldcinema.data.dto.CollectionFormDto
import com.example.worldcinema.data.dto.MovieValueDto
import com.example.worldcinema.domain.model.CollectionInfo
import com.example.worldcinema.domain.model.Movie
import com.example.worldcinema.domain.repository.CollectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CollectionRepositoryImpl @Inject constructor(
    private val api: CollectionApi
) : CollectionRepository {
    override fun getCollections(): Flow<Result<List<CollectionInfo>>> = flow {
        try {
            val collectionList = api.getCollections().map { it.toCollectionInfo() }
            emit(Result.success(collectionList))
        } catch (e: Exception) {
            Log.e("getCollections", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun createCollection(nameCollection: String): Flow<Result<CollectionInfo>> = flow {
        try {
            val createdCollection = api.createCollection(CollectionFormDto(nameCollection))
                .toCollectionInfo()
            emit(Result.success(createdCollection))
        } catch (e: Exception) {
            Log.e("OPS createCollection", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun deleteCollection(collectionId: String): Flow<Result<Unit>> = flow {
        try {
            api.deleteCollection(collectionId)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            Log.e("OPS deleteCollection", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun getCollectionFilms(collectionId: String): Flow<Result<List<Movie>>> = flow {
        try {
            val movieList = api.getCollectionFilms(collectionId).map { it.toMovie() }
            emit(Result.success(movieList))
        } catch (e: Exception) {
            Log.e("OPS getCollectionFilms", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun addFilmInCollection(collectionId: String, movieId: String): Flow<Result<Unit>> = flow {
        try {
            api.addFilmInCollection(collectionId, MovieValueDto(movieId))
            emit(Result.success(Unit))
        } catch (e: Exception) {
            Log.e("OPS addFilmInCollection", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun deleteFilmFromCollection(
        collectionId: String,
        movieId: String
    ): Flow<Result<Unit>> = flow {
        try {
            api.deleteFilmFromCollection(collectionId, movieId)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            Log.e("OPS deleteFilmFromCollection", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}
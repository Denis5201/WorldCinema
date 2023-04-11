package com.example.worldcinema.data.repository

import android.util.Log
import com.example.worldcinema.data.api.CollectionApi
import com.example.worldcinema.data.api.ProfileApi
import com.example.worldcinema.data.dao.CollectionDao
import com.example.worldcinema.data.dto.CollectionFormDto
import com.example.worldcinema.data.dto.MovieValueDto
import com.example.worldcinema.data.entity.CollectionEntity
import com.example.worldcinema.domain.model.CollectionInfo
import com.example.worldcinema.domain.model.Movie
import com.example.worldcinema.domain.repository.CollectionRepository
import com.example.worldcinema.domain.repository.SharedPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CollectionRepositoryImpl @Inject constructor(
    private val collectionApi: CollectionApi,
    private val profileApi: ProfileApi,
    private val dao: CollectionDao,
    private val preferencesRepository: SharedPreferencesRepository
) : CollectionRepository {
    override fun getCollections(): Flow<Result<List<CollectionInfo>>> = flow {
        try {
            val collectionList = collectionApi.getCollections().map { it.toCollectionInfo() }.toMutableList()

            val userId = getUserId()
            val localCollectionList = dao.getUserCollectionList(userId)

            collectionList.forEach { remote ->
                val collectionDb = localCollectionList.find { it.collectionId == remote.collectionId}
                if (collectionDb != null) {
                    remote.image = collectionDb.image
                    remote.name = collectionDb.name
                }
            }

            emit(Result.success(collectionList))
        } catch (e: Exception) {
            Log.e("getCollections", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun createCollection(
        nameCollection: String,
        imageCollection: String?,
    ): Flow<Result<CollectionInfo>> = flow {
        try {
            val createdCollection = collectionApi.createCollection(CollectionFormDto(nameCollection))
                .toCollectionInfo()

            val userId = getUserId()

            dao.addCollection(CollectionEntity(
                collectionId = createdCollection.collectionId,
                userId = userId,
                name = nameCollection,
                image = imageCollection
            ))
            emit(Result.success(createdCollection))
        } catch (e: Exception) {
            Log.e("OPS createCollection", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateCollection(collectionInfo: CollectionInfo) {
        val userId = getUserId()

        dao.updateCollection(
            CollectionEntity(
                collectionId = collectionInfo.collectionId,
                userId = userId,
                name = collectionInfo.name,
                image = collectionInfo.image
            )
        )
    }

    override fun deleteCollection(collectionId: String): Flow<Result<Unit>> = flow {
        try {
            collectionApi.deleteCollection(collectionId)
            dao.deleteCollection(collectionId)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            Log.e("OPS deleteCollection", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun getCollectionFilms(collectionId: String): Flow<Result<List<Movie>>> = flow {
        try {
            val movieList = collectionApi.getCollectionFilms(collectionId).map { it.toMovie() }
            emit(Result.success(movieList))
        } catch (e: Exception) {
            Log.e("OPS getCollectionFilms", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun addFilmInCollection(collectionId: String, movieId: String): Flow<Result<Unit>> = flow {
        try {
            collectionApi.addFilmInCollection(collectionId, MovieValueDto(movieId))
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
            collectionApi.deleteFilmFromCollection(collectionId, movieId)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            Log.e("OPS deleteFilmFromCollection", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun getUserId(): String {
        var userId = preferencesRepository.getUserId()
        if (userId.isNullOrEmpty()) {
            userId = profileApi.getUserAccount().userId
        }
        return userId
    }
}
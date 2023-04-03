package com.example.worldcinema.data.repository

import android.util.Log
import com.example.worldcinema.data.api.EpisodeApi
import com.example.worldcinema.data.dto.EpisodeTimeDto
import com.example.worldcinema.domain.model.EpisodeTime
import com.example.worldcinema.domain.repository.EpisodeTimeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class EpisodeTimeRepositoryImpl @Inject constructor(
    private val api: EpisodeApi
) : EpisodeTimeRepository {

    override fun getTimePosition(episodeId: String): Flow<Result<EpisodeTime>> = flow {
        try {
            val time = api.getTimePosition(episodeId).toEpisodeTime()
            emit(Result.success(time))
        } catch (e: Exception) {
            Log.e("OPS getTimePosition", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun saveTimePosition(episodeId: String, time: Long): Flow<Result<Unit>> = flow {
        try {
            api.saveTimePosition(episodeId, EpisodeTimeDto(time))
            emit(Result.success(Unit))
        } catch (e: Exception) {
            Log.e("OPS saveTimePosition", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}
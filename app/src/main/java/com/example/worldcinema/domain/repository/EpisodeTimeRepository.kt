package com.example.worldcinema.domain.repository

import com.example.worldcinema.domain.model.EpisodeTime
import kotlinx.coroutines.flow.Flow

interface EpisodeTimeRepository {

    fun getTimePosition(episodeId: String): Flow<Result<EpisodeTime>>

    fun saveTimePosition(episodeId: String, time: Long): Flow<Result<Unit>>
}
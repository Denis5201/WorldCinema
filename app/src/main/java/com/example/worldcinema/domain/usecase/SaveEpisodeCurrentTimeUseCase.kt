package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.repository.EpisodeTimeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveEpisodeCurrentTimeUseCase @Inject constructor(
    private val repository: EpisodeTimeRepository
) {

    operator fun invoke(episodeId: String, timeSeconds: Long): Flow<Result<Unit>> =
        repository.saveTimePosition(episodeId, timeSeconds)
}
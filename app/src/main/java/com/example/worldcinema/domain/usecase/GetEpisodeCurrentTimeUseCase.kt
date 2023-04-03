package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.model.EpisodeTime
import com.example.worldcinema.domain.repository.EpisodeTimeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEpisodeCurrentTimeUseCase @Inject constructor(
    private val repository: EpisodeTimeRepository
) {

    operator fun invoke(episodeId: String): Flow<Result<EpisodeTime>> =
        repository.getTimePosition(episodeId)
}
package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.model.Episode
import com.example.worldcinema.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEpisodesUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    operator fun invoke(movieId: String):Flow<Result<List<Episode>>> =
        repository.getEpisodes(movieId)
}
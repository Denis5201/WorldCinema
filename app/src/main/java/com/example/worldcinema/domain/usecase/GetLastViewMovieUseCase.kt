package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.model.EpisodeHistory
import com.example.worldcinema.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLastViewMovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    operator fun invoke(): Flow<Result<List<EpisodeHistory>>> = repository.getHistory()
}
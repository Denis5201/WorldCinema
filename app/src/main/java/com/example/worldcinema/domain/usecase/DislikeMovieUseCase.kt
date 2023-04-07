package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DislikeMovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    operator fun invoke(movieId: String): Flow<Result<Unit>> = repository.dislike(movieId)
}
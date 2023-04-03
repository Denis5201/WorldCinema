package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.model.Movie
import com.example.worldcinema.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLastViewMovieByIdUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    operator fun invoke(movieId: String): Flow<Result<Movie>> = repository.getMovieById(movieId)
}
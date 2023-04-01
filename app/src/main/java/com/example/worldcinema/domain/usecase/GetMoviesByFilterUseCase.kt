package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.model.FilterMain
import com.example.worldcinema.domain.model.Movie
import com.example.worldcinema.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesByFilterUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    operator fun invoke(filterMain: FilterMain): Flow<Result<List<Movie>>> =
        repository.getMovies(filterMain)
}
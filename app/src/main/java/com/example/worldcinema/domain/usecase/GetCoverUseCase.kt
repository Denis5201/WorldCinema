package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.model.Cover
import com.example.worldcinema.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoverUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    operator fun invoke(): Flow<Result<Cover>> = repository.getCover()
}
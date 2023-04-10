package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.model.Movie
import com.example.worldcinema.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilmsCollectionUseCase @Inject constructor(
    private val repository: CollectionRepository
) {

    operator fun invoke(collectionId: String): Flow<Result<List<Movie>>> =
        repository.getCollectionFilms(collectionId)
}
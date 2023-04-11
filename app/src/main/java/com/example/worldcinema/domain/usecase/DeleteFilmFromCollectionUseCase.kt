package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.repository.CollectionRepository
import javax.inject.Inject

class DeleteFilmFromCollectionUseCase @Inject constructor(
    private val repository: CollectionRepository
) {

    operator fun invoke(collectionId: String, movieId: String) =
        repository.deleteFilmFromCollection(collectionId, movieId)
}
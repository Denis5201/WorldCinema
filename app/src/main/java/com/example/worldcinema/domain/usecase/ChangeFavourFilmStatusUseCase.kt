package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChangeFavourFilmStatusUseCase @Inject constructor(
    private val repository: CollectionRepository
) {

    operator fun invoke(
        favourCollectionId: String, movieId: String, isFavour: Boolean
    ): Flow<Result<Unit>> {
        return if (isFavour) {
            repository.deleteFilmFromCollection(favourCollectionId, movieId)
        } else {
            repository.addFilmInCollection(favourCollectionId, movieId)
        }
    }
}
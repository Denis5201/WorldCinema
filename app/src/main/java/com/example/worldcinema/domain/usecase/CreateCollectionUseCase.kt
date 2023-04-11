package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.model.CollectionInfo
import com.example.worldcinema.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateCollectionUseCase @Inject constructor(
    private val repository: CollectionRepository
) {

    operator fun invoke(nameCollection: String, imageCollection: String?): Flow<Result<CollectionInfo>> =
        repository.createCollection(nameCollection, imageCollection)
}
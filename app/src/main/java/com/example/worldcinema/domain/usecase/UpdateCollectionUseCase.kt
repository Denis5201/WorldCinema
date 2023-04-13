package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.model.CollectionInfo
import com.example.worldcinema.domain.repository.CollectionRepository
import javax.inject.Inject

class UpdateCollectionUseCase @Inject constructor(
    private val repository: CollectionRepository
) {

    suspend operator fun invoke(collectionInfo: CollectionInfo) =
        repository.upsertCollection(collectionInfo)
}
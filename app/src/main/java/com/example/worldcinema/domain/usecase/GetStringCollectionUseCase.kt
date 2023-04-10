package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.model.CollectionInfo
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class GetStringCollectionUseCase @Inject constructor() {

    operator fun invoke(collection: CollectionInfo): String = Json.encodeToString(collection)
}
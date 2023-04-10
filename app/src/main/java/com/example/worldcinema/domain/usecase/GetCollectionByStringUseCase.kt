package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.model.CollectionInfo
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class GetCollectionByStringUseCase @Inject constructor() {

    operator fun invoke(collectionString: String): CollectionInfo = Json.decodeFromString(collectionString)
}
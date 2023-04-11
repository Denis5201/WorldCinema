package com.example.worldcinema.domain.usecase

import com.example.worldcinema.Constants
import javax.inject.Inject

class IsCollectionNameValidUseCase @Inject constructor() {

    operator fun invoke(collectionName: String): Boolean {
        return (collectionName != Constants.FAVOUR_COLLECTION &&
                collectionName.lowercase() != Constants.FAVOUR_COLLECTION)
    }
}
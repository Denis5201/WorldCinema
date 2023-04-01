package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.model.Movie
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class GetMovieByStringUseCase @Inject constructor() {

    operator fun invoke(movieString: String): Movie = Json.decodeFromString(movieString)
}
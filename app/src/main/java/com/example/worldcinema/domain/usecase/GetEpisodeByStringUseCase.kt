package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.model.Episode
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class GetEpisodeByStringUseCase @Inject constructor() {

    operator fun invoke(episodeString: String): Episode = Json.decodeFromString(episodeString)
}
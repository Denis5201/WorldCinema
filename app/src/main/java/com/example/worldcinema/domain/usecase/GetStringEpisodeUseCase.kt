package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.model.Episode
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class GetStringEpisodeUseCase @Inject constructor() {

    operator fun invoke(episode: Episode): String = Json.encodeToString(episode)
}
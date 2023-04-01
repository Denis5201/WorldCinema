package com.example.worldcinema.data.dto

import com.example.worldcinema.domain.model.Episode
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeDto(
    val episodeId: String,
    val name: String,
    val description: String,
    val director: String,
    val stars: List<String>,
    val year: String,
    val images: List<String>,
    val runtime: Int,
    val preview: String,
    val filePath: String
) {
    fun toEpisode(): Episode {
        return Episode(
            episodeId = episodeId,
            name = name,
            description = description,
            year = year,
            runtime = runtime,
            preview = preview,
            filePath = filePath
        )
    }
}

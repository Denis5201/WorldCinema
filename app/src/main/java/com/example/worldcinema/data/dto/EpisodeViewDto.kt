package com.example.worldcinema.data.dto

import com.example.worldcinema.domain.model.EpisodeHistory
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeViewDto(
    val episodeId: String,
    val movieId: String,
    val episodeName: String,
    val movieName: String,
    val preview: String,
    val filePath: String,
    val time: Int
) {
    fun toEpisodeHistory(): EpisodeHistory {
        return EpisodeHistory(
            episodeId = episodeId,
            movieId = movieId,
            episodeName = episodeName,
            movieName = movieName,
            preview = preview,
            filePath = filePath,
            time = time
        )
    }
}

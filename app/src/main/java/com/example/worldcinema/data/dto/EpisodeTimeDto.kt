package com.example.worldcinema.data.dto

import com.example.worldcinema.domain.model.EpisodeTime
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeTimeDto(
    val timeInSeconds: Long?
) {
    fun toEpisodeTime(): EpisodeTime {
        return EpisodeTime(timeInSeconds = timeInSeconds)
    }
}

package com.example.worldcinema.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MovieValueDto(
    val movieId: String
) {
    companion object {
        fun fromString(movieId: String): MovieValueDto {
            return MovieValueDto(
                movieId = movieId
            )
        }
    }
}

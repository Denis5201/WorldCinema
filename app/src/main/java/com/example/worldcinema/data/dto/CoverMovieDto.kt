package com.example.worldcinema.data.dto

import com.example.worldcinema.domain.model.Cover
import kotlinx.serialization.Serializable

@Serializable
data class CoverMovieDto(
    val backgroundImage: String?,
    val foregroundImage: String?
) {
    fun toCover(): Cover {
        return Cover(
            image = backgroundImage
        )
    }
}

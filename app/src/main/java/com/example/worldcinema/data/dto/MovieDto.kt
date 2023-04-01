package com.example.worldcinema.data.dto

import com.example.worldcinema.domain.model.Chat
import com.example.worldcinema.domain.model.Movie
import com.example.worldcinema.domain.model.Tag
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    val movieId: String,
    val name: String,
    val description: String,
    val age: String,
    val chatInfo: ChatDto?,
    val imageUrls: List<String>,
    val poster: String,
    val tags: List<TagDto>
) {
    fun toMovie(): Movie {
        return Movie(
            movieId = movieId,
            name = name,
            description = description,
            age = age,
            chatInfo = if (chatInfo != null) {
                Chat(chatId = chatInfo.chatId, name = chatInfo.chatName)
            } else {
                null
            },
            imageUrls = imageUrls,
            poster = poster,
            tags = tags.map {
                Tag(
                    tagId = it.tagId,
                    name = it.tagName
                )
            }
        )
    }
}
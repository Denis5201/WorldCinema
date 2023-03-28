package com.example.worldcinema.data.dto

import com.example.worldcinema.domain.model.UserAccount
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val avatar: String?
) {
    fun toUserAccount(): UserAccount {
        return UserAccount(
            userId = userId,
            userName = "$firstName $lastName",
            email = email,
            avatar = avatar
        )
    }
}

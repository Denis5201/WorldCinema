package com.example.worldcinema.data.dto

import com.example.worldcinema.domain.model.Credentials
import kotlinx.serialization.Serializable

@Serializable
data class CredentialsDto(
    val email: String,
    val password: String
) {
    companion object {
        fun fromCredentials(credentials: Credentials): CredentialsDto {
            return CredentialsDto(
                email = credentials.email,
                password = credentials.password
            )
        }
    }
}

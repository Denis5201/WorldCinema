package com.example.worldcinema.domain.model

data class UserAccount(
    val userId: String,
    val userName: String,
    val email: String,
    val avatar: String?
)
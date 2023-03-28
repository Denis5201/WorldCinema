package com.example.worldcinema.presentation.login

data class RegistrationInfo(
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)

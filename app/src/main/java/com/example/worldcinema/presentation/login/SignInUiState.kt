package com.example.worldcinema.presentation.login

data class SignInUiState(
    val isLoading: Boolean = true,
    val makingRequest: Boolean = false,
    val isShowMessage: Boolean = false,
    val message: String = "",
    val mayNavigateToMain: Boolean = false,
    val mustNavigateToRegister: Boolean = false
)

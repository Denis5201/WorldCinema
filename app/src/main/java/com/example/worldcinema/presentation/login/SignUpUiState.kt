package com.example.worldcinema.presentation.login

data class SignUpUiState(
    val isLoading: Boolean = false,
    val isShowMessage: Boolean = false,
    val message: String = "",
    val mayNavigateToMain: Boolean = false
)

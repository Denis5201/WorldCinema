package com.example.worldcinema.presentation.profile

data class ProfileUiState(
    val isLoading: Boolean = true,
    val isShowMessage: Boolean = false,
    val message: String = "",
    val loadedAvatar: Boolean = false,
)

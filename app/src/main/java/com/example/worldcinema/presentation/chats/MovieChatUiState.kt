package com.example.worldcinema.presentation.chats

data class MovieChatUiState(
    val isLoading: Boolean = true,
    val isShowMessage: Boolean = false,
    val message: String = ""
)

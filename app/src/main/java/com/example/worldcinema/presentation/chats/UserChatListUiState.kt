package com.example.worldcinema.presentation.chats

data class UserChatListUiState(
    val isLoading: Boolean = true,
    val goToMovieChat: Boolean = false,
    val isShowMessage: Boolean = false,
    val message: String = ""
)
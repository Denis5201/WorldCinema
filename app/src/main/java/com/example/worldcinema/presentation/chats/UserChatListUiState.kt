package com.example.worldcinema.presentation.chats

data class UserChatListUiState(
    val isLoading: Boolean = true,
    val goToMovieChat: Boolean = false,
    val chatId: String = "",
    val chatName: String = "",
    val isShowMessage: Boolean = false,
    val message: String = ""
)
package com.example.worldcinema.presentation.movie

data class EpisodeUiState(
    val goToChatScreen: Boolean = false,
    val mayNavigateBack: Boolean = false,
    val chatId: String = "",
    val chatName: String = "",
    val makingRequest: Boolean = false,
    val isShowMessage: Boolean = false,
    val message: String = "",
    val isLoadingEpisode: Boolean = false
)
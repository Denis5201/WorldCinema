package com.example.worldcinema.presentation.base

data class MainUiState(
    val goWatchEpisode: Boolean = false,
    val goToFilmScreen: Boolean = false,
    val movieString: String = "",
    val episodeId: String = "",
    val movieId: String = "",
    val isShowMessage: Boolean = false,
    val message: String = ""
)
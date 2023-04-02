package com.example.worldcinema.presentation.movie

data class FilmUiState(
    val goToEpisodeScreen: Boolean = false,
    val movieString: String = "",
    val episodeId: String = "",
    val episodeString: String = "",
    val isShowMessage: Boolean = false,
    val message: String = ""
)
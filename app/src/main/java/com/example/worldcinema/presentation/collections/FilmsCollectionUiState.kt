package com.example.worldcinema.presentation.collections

data class FilmsCollectionUiState(
    val isLoading: Boolean = true,
    val goToEditCollection: Boolean = false,
    val goToFilm: Boolean = false,
    val movieString: String = "",
    val isShowMessage: Boolean = false,
    val message: String = ""
)

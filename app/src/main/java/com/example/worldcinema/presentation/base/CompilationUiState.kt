package com.example.worldcinema.presentation.base

data class CompilationUiState(
    val isLoading: Boolean = false,
    val goToFilmScreen: Boolean = false,
    val movieString: String = "",
    val isShowMessage: Boolean = false,
    val message: String = ""
)

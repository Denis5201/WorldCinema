package com.example.worldcinema.presentation.collections

data class FilmsCollectionUiState(
    val isLoading: Boolean = true,
    val goToEditCollection: Boolean = false,
    val collectionString: String = "",
    val isShowMessage: Boolean = false,
    val message: String = ""
)

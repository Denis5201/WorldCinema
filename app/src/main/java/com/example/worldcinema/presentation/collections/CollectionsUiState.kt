package com.example.worldcinema.presentation.collections

data class CollectionsUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val goToFilmsCollection: Boolean = false,
    val goToCreateCollection: Boolean = false,
    val collectionString: String = "",
    val isShowMessage: Boolean = false,
    val message: String = ""
)
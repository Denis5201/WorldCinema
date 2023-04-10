package com.example.worldcinema.presentation.collections

data class EditCollectionUiState(
    val goBackAfterSave: Boolean = false,
    val isShowMessage: Boolean = false,
    val message: String = ""
)
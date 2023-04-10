package com.example.worldcinema.presentation.collections

import androidx.lifecycle.*
import com.example.worldcinema.MessageSource
import com.example.worldcinema.domain.model.CollectionInfo
import com.example.worldcinema.domain.model.Movie
import com.example.worldcinema.domain.usecase.GetCollectionByStringUseCase
import com.example.worldcinema.domain.usecase.GetFilmsCollectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmsCollectionViewModel @Inject constructor(
    private val getFilmsCollectionUseCase: GetFilmsCollectionUseCase,
    getCollectionByStringUseCase: GetCollectionByStringUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableLiveData(FilmsCollectionUiState())
    val uiState: LiveData<FilmsCollectionUiState> = _uiState

    private val _filmsCollection = MutableLiveData(emptyList<Movie>())
    val filmsCollection: LiveData<List<Movie>> = _filmsCollection

    val collectionInfo: CollectionInfo

    init {
        collectionInfo = getCollectionByStringUseCase(
            FilmsCollectionFragmentArgs.fromSavedStateHandle(savedStateHandle).collectionString
        )
        getFilmsCollection(collectionInfo.collectionId)
    }

    fun setDefaultStatus() {
        _uiState.value = _uiState.value!!.copy(
            isLoading = false,
            goToEditCollection = false,
            isShowMessage = false
        )
    }

    private fun getFilmsCollection(collectionId: String) {
        viewModelScope.launch {
            getFilmsCollectionUseCase(collectionId).collect { result ->
                result.onSuccess {
                    _filmsCollection.value = it
                    _uiState.value = _uiState.value!!.copy(
                        isLoading = false,
                    )
                }.onFailure {
                    _uiState.value = _uiState.value!!.copy(
                        isLoading = false,
                        isShowMessage = true,
                        message = it.message ?: MessageSource.ERROR
                    )
                }
            }
        }
    }
}
package com.example.worldcinema.presentation.collections

import androidx.lifecycle.*
import com.example.worldcinema.MessageSource
import com.example.worldcinema.domain.model.CollectionInfo
import com.example.worldcinema.domain.model.Movie
import com.example.worldcinema.domain.usecase.DeleteFilmFromCollectionUseCase
import com.example.worldcinema.domain.usecase.GetCollectionByStringUseCase
import com.example.worldcinema.domain.usecase.GetFilmsCollectionUseCase
import com.example.worldcinema.domain.usecase.GetStringMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmsCollectionViewModel @Inject constructor(
    private val getFilmsCollectionUseCase: GetFilmsCollectionUseCase,
    private val deleteFilmFromCollectionUseCase: DeleteFilmFromCollectionUseCase,
    private val getStringMovieUseCase: GetStringMovieUseCase,
    getCollectionByStringUseCase: GetCollectionByStringUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableLiveData(FilmsCollectionUiState())
    val uiState: LiveData<FilmsCollectionUiState> = _uiState

    private val _filmsCollection = MutableLiveData(emptyList<Movie>())
    val filmsCollection: LiveData<List<Movie>> = _filmsCollection

    val collectionInfo: CollectionInfo
    val collectionString: String

    init {
        collectionString = FilmsCollectionFragmentArgs.fromSavedStateHandle(savedStateHandle).collectionString
        collectionInfo = getCollectionByStringUseCase(collectionString)
        getFilmsCollection(collectionInfo.collectionId)
    }

    fun deleteFilm(movieId: String) {
        viewModelScope.launch {
            deleteFilmFromCollectionUseCase(collectionInfo.collectionId, movieId).collect { result ->
                result.onSuccess {

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

    fun setDefaultStatus() {
        _uiState.value = _uiState.value!!.copy(
            isLoading = false,
            goToEditCollection = false,
            goToFilm = false,
            isShowMessage = false
        )
    }

    fun toEditCollection() {
        _uiState.value = _uiState.value!!.copy(
            goToEditCollection = true
        )
    }

    fun toFilm(movie: Movie) {
        _uiState.value = _uiState.value!!.copy(
            goToFilm = true,
            movieString = getStringMovieUseCase(movie)
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
package com.example.worldcinema.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldcinema.Constants
import com.example.worldcinema.MessageSource
import com.example.worldcinema.domain.model.FilterMain
import com.example.worldcinema.domain.model.Movie
import com.example.worldcinema.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompilationViewModel @Inject constructor(
    private val getMoviesByFilterUseCase: GetMoviesByFilterUseCase,
    private val getStringMovieUseCase: GetStringMovieUseCase,
    private val dislikeMovieUseCase: DislikeMovieUseCase,
    private val getCollectionsUseCase: GetCollectionsUseCase,
    private val getFilmsCollectionUseCase: GetFilmsCollectionUseCase,
    private val isFilmInFavourUseCase: IsFilmInFavourUseCase,
    private val changeFavourFilmStatusUseCase: ChangeFavourFilmStatusUseCase,
    private val messageSource: MessageSource
) : ViewModel() {

    private val _uiState = MutableLiveData(CompilationUiState())
    val uiState: LiveData<CompilationUiState> = _uiState

    private val _compilation = MutableLiveData(emptyList<Movie>())
    val compilation: LiveData<List<Movie>> = _compilation

    var swipedCounter = 0
        private set

    private val _alreadyInFavour = MutableLiveData<Boolean>()
    val alreadyInFavour: LiveData<Boolean> = _alreadyInFavour

    private var favourId: String = ""
    private val _listFavourMovie = MutableLiveData(emptyList<Movie>())
    val listFavourMovie: LiveData<List<Movie>> = _listFavourMovie
    private var isChangingStatus = false

    init {
        getCompilation()
    }

    fun itemSwiped() {
        swipedCounter++
    }

    fun toFilmScreen(movie: Movie) {
        _uiState.value = _uiState.value!!.copy(
            goToFilmScreen = true,
            movieString = getStringMovieUseCase(movie)
        )
    }

    fun dislike(movieId: String) {
        viewModelScope.launch {
            dislikeMovieUseCase(movieId).collect { result ->
                result.onSuccess {

                }.onFailure {
                    _uiState.value = _uiState.value!!.copy(
                        isShowMessage = true,
                        message = it.message ?: MessageSource.ERROR
                    )
                }
            }
        }
    }

    fun getFavourFilms() {
        if (swipedCounter == (_compilation.value?.size ?: 0) || favourId.isEmpty()) {
            return
        }
        viewModelScope.launch {
            getFilmsCollectionUseCase(favourId).collect { result ->
                result.onSuccess {
                    _listFavourMovie.value = it
                }.onFailure {
                    _uiState.value = _uiState.value!!.copy(
                        isShowMessage = true,
                        message = it.message ?: MessageSource.ERROR
                    )
                }
            }
        }
    }

    fun isFilmInFavour(movieId: String) {
        _alreadyInFavour.value = listFavourMovie.value?.let { isFilmInFavourUseCase(movieId, it) }
    }

    fun changeFilmFavourStatus(movieId: String) {
        if (favourId.isEmpty()) {
            _uiState.value = _uiState.value!!.copy(
                isShowMessage = true,
                message = messageSource.getMessage(MessageSource.FAVOUR_COLLECTION_NOT_FOUND)
            )
            return
        }
        if (isChangingStatus) {
            return
        }
        isChangingStatus = true
        viewModelScope.launch {
            changeFavourFilmStatusUseCase(
                favourId, movieId, alreadyInFavour.value!!
            ).collect { result ->
                result.onSuccess {
                    _alreadyInFavour.value = !(_alreadyInFavour.value!!)
                    isChangingStatus = false
                }.onFailure {
                    _uiState.value = _uiState.value!!.copy(
                        isShowMessage = true,
                        message = it.message ?: MessageSource.ERROR
                    )
                    isChangingStatus = false
                }
            }
        }
    }

    fun setDefaultStatus() {
        _uiState.value = _uiState.value!!.copy(
            isLoading = false,
            isShowMessage = false,
            goToFilmScreen = false
        )
    }

    private fun getCompilation() {
        _uiState.value = _uiState.value!!.copy(
            isLoading = true
        )
        viewModelScope.launch {
            getMoviesByFilterUseCase(FilterMain.COMPILATION).collect { result ->
                result.onSuccess {
                    _compilation.value = it
                    _uiState.value = _uiState.value!!.copy(
                        isLoading = false
                    )
                    findFavourCollectionId()
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

    private fun findFavourCollectionId() {
        viewModelScope.launch {
            getCollectionsUseCase().collect { result ->
                result.onSuccess { list ->
                    val favour =
                        list.find { it.name == Constants.FAVOUR_COLLECTION }
                    if (favour != null) {
                        favourId = favour.collectionId
                        getFavourFilms()
                    }
                }.onFailure {
                    _uiState.value = _uiState.value!!.copy(
                        isShowMessage = true,
                        message = it.message ?: MessageSource.ERROR
                    )
                }
            }
        }
    }
}
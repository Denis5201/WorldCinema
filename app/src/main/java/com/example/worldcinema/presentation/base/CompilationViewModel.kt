package com.example.worldcinema.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldcinema.MessageSource
import com.example.worldcinema.domain.model.FilterMain
import com.example.worldcinema.domain.model.Movie
import com.example.worldcinema.domain.usecase.DislikeMovieUseCase
import com.example.worldcinema.domain.usecase.GetMoviesByFilterUseCase
import com.example.worldcinema.domain.usecase.GetStringMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompilationViewModel @Inject constructor(
    private val getMoviesByFilterUseCase: GetMoviesByFilterUseCase,
    private val getStringMovieUseCase: GetStringMovieUseCase,
    private val dislikeMovieUseCase: DislikeMovieUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData(CompilationUiState())
    val uiState: LiveData<CompilationUiState> = _uiState

    private val _compilation = MutableLiveData(emptyList<Movie>())
    val compilation: LiveData<List<Movie>> = _compilation

    var swipedCounter = 0
        private set

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
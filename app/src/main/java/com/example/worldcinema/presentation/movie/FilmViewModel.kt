package com.example.worldcinema.presentation.movie

import androidx.lifecycle.*
import com.example.worldcinema.MessageSource
import com.example.worldcinema.domain.model.Episode
import com.example.worldcinema.domain.model.Movie
import com.example.worldcinema.domain.usecase.GetAgeColorStringUseCase
import com.example.worldcinema.domain.usecase.GetEpisodesUseCase
import com.example.worldcinema.domain.usecase.GetMovieByStringUseCase
import com.example.worldcinema.domain.usecase.GetStringEpisodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmViewModel @Inject constructor(
    private val getEpisodesUseCase: GetEpisodesUseCase,
    private val getStringEpisodeUseCase: GetStringEpisodeUseCase,
    getMovieByStringUseCase: GetMovieByStringUseCase,
    getAgeColorStringUseCase: GetAgeColorStringUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableLiveData(FilmUiState())
    val uiState: LiveData<FilmUiState> = _uiState

    private val _episodes = MutableLiveData(emptyList<Episode>())
    val episodes: LiveData<List<Episode>> = _episodes

    val movieString: String
    val movie: Movie
    val ageColor: String

    init {
        movieString = FilmFragmentArgs.fromSavedStateHandle(savedStateHandle).movie
        movie = getMovieByStringUseCase(movieString)
        ageColor = getAgeColorStringUseCase(movie.age)
        getEpisodes()
    }

    fun toEpisodeScreen(episodeId: String) {
        _uiState.value = _uiState.value!!.copy(
            goToEpisodeScreen = true,
            episodeId = episodeId,
            episodeString = getStringEpisodeUseCase(_episodes.value!!.find { it.episodeId == episodeId }!!)
        )
    }

    fun setDefaultStatus() {
        _uiState.value = _uiState.value!!.copy(
            goToEpisodeScreen = false,
            isShowMessage = false
        )
    }

    private fun getEpisodes() {
        viewModelScope.launch {
            getEpisodesUseCase(movie.movieId).collect { result ->
                result.onSuccess {
                    _episodes.value = it
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
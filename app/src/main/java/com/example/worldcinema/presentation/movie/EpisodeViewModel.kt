package com.example.worldcinema.presentation.movie

import androidx.lifecycle.*
import com.example.worldcinema.MessageSource
import com.example.worldcinema.domain.model.Episode
import com.example.worldcinema.domain.model.Movie
import com.example.worldcinema.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodeViewModel @Inject constructor(
    private val getEpisodesUseCase: GetEpisodesUseCase,
    private val getLastViewMovieByIdUseCase: GetLastViewMovieByIdUseCase,
    private val getEpisodeCurrentTimeUseCase: GetEpisodeCurrentTimeUseCase,
    private val saveEpisodeCurrentTimeUseCase: SaveEpisodeCurrentTimeUseCase,
    getMovieByStringUseCase: GetMovieByStringUseCase,
    getEpisodeByStringUseCase: GetEpisodeByStringUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableLiveData(EpisodeUiState())
    val uiState: LiveData<EpisodeUiState> = _uiState

    private val _movie = MutableLiveData<Movie>(null)
    val movie: LiveData<Movie> = _movie

    private val _episode = MutableLiveData<Episode>(null)
    val episode: LiveData<Episode> = _episode

    private val _timePosition = MutableLiveData<Long>()
    val timePosition: LiveData<Long> = _timePosition

    init {
        val args = EpisodeFragmentArgs.fromSavedStateHandle(savedStateHandle)
        val movieString = args.movie
        val episodeString = args.episode
        if (movieString.isNullOrEmpty() || episodeString.isNullOrEmpty()) {
            getEpisodeById(args.episodeId, args.movieId)
            getMovieById(args.movieId)
        } else {
            _movie.value = getMovieByStringUseCase(movieString)
            _episode.value = getEpisodeByStringUseCase(episodeString)
            getVideoPosition(_episode.value!!.episodeId)
        }
    }

    fun saveVideoPosition(time: Long, reason: Int) {
        if (_episode.value == null) {
            return
        }
        if (time <= 0 || time / 1000 == _timePosition.value) {
            setNavigation(reason)
        }
        val timeInSeconds = time / 1000
        viewModelScope.launch {
            saveEpisodeCurrentTimeUseCase(
                _episode.value!!.episodeId, timeInSeconds
            ).collect { result ->
                result.onSuccess {
                    setNavigation(reason)
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
            goToChatScreen = false,
            mayNavigateBack = false,
            isShowMessage = false
        )
    }

    private fun getVideoPosition(episodeId: String) {
        viewModelScope.launch {
            getEpisodeCurrentTimeUseCase(episodeId).collect { result ->
                result.onSuccess {
                    if (it.timeInSeconds != null) {
                        _timePosition.value = it.timeInSeconds!!
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

    private fun getEpisodeById(episodeId: String, movieId: String) {
        viewModelScope.launch {
            getEpisodesUseCase(movieId).collect { result ->
                result.onSuccess { episodeList ->
                    _episode.value = episodeList.find { it.episodeId == episodeId }
                    getVideoPosition(episodeId)
                }.onFailure {
                    _uiState.value = _uiState.value!!.copy(
                        isShowMessage = true,
                        message = it.message ?: MessageSource.ERROR
                    )
                }
            }
        }
    }

    private fun getMovieById(movieId: String) {
        viewModelScope.launch {
            getLastViewMovieByIdUseCase(movieId).collect {result ->
                result.onSuccess {
                    _movie.value = it
                }.onFailure {
                    _uiState.value = _uiState.value!!.copy(
                        isShowMessage = true,
                        message = it.message ?: MessageSource.ERROR
                    )
                }
            }
        }
    }

    private fun setNavigation(reason: Int) {
        if (reason == CHAT) {
            _uiState.value = _uiState.value!!.copy(
                goToChatScreen = true,
                chatId = _movie.value!!.chatInfo!!.chatId
            )
        } else if (reason == BACK) {
            _uiState.value = _uiState.value!!.copy(
                mayNavigateBack = true
            )
        }
    }

    companion object {
        const val BACK = 0
        const val CHAT = 1
    }
}
package com.example.worldcinema.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldcinema.MessageSource
import com.example.worldcinema.domain.model.EpisodeHistory
import com.example.worldcinema.domain.model.FilterMain
import com.example.worldcinema.domain.model.Movie
import com.example.worldcinema.domain.usecase.GetCoverUseCase
import com.example.worldcinema.domain.usecase.GetLastViewMovieUseCase
import com.example.worldcinema.domain.usecase.GetMoviesByFilterUseCase
import com.example.worldcinema.domain.usecase.GetStringMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCoverUseCase: GetCoverUseCase,
    private val getMoviesByFilterUseCase: GetMoviesByFilterUseCase,
    private val getLastViewMovieUseCase: GetLastViewMovieUseCase,
    private val getStringMovieUseCase: GetStringMovieUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData(MainUiState())
    val uiState: LiveData<MainUiState> = _uiState

    private val _coverImage = MutableLiveData("")
    val coverImage: LiveData<String> = _coverImage

    private val _trendList = MutableLiveData(emptyList<Movie>())
    val trendList: LiveData<List<Movie>> = _trendList

    private val _lastViewEpisode = MutableLiveData<EpisodeHistory?>()
    val lastViewEpisode: LiveData<EpisodeHistory?> = _lastViewEpisode

    private val _newList = MutableLiveData(emptyList<Movie>())
    val newList: LiveData<List<Movie>> = _newList

    private val _forMeList = MutableLiveData(emptyList<Movie>())
    val forMeList: LiveData<List<Movie>> = _forMeList

    init {
        _uiState.value = MainUiState()
        getCover()
        getTrendMovies()
        getLastView()
        getNewMovies()
        getForMeMovies()
    }

    fun goWatchEpisode() {
        if (_lastViewEpisode.value != null) {
            _uiState.value = _uiState.value!!.copy(
                goWatchEpisode = true,
                movieId = _lastViewEpisode.value!!.movieId,
                episodeId = _lastViewEpisode.value!!.episodeId
            )
        }
    }

    fun toFilmScreen(movie: Movie) {
        _uiState.value = _uiState.value!!.copy(
            goToFilmScreen = true,
            movieString = getStringMovieUseCase(movie)
        )
    }

    fun setDefaultStatus() {
        _uiState.value = _uiState.value!!.copy(
            isShowMessage = false,
            goWatchEpisode = false,
            goToFilmScreen = false
        )
    }

    private fun getCover() {
        viewModelScope.launch {
            getCoverUseCase().collect { result ->
                result.onSuccess {
                    _coverImage.value = it.image
                }.onFailure {
                    _uiState.value = _uiState.value!!.copy(
                        isShowMessage = true,
                        message = it.message ?: MessageSource.ERROR
                    )
                }
            }
        }
    }

    private fun getTrendMovies() {
        viewModelScope.launch {
            getMoviesByFilterUseCase(FilterMain.IN_TREND).collect { result ->
                result.onSuccess {
                    _trendList.value = it
                }.onFailure {
                    _uiState.value = _uiState.value!!.copy(
                        isShowMessage = true,
                        message = it.message ?: MessageSource.ERROR
                    )
                }
            }
        }
    }

    private fun getLastView() {
        viewModelScope.launch {
            getLastViewMovieUseCase().collect { result ->
                result.onSuccess {
                    _lastViewEpisode.value = if (it.isEmpty()) null else it.first()
                }.onFailure {
                    _uiState.value = _uiState.value!!.copy(
                        isShowMessage = true,
                        message = it.message ?: MessageSource.ERROR
                    )
                }
            }
        }
    }

    private fun getNewMovies() {
        viewModelScope.launch {
            getMoviesByFilterUseCase(FilterMain.NEW).collect { result ->
                result.onSuccess {
                    _newList.value = it
                }.onFailure {
                    _uiState.value = _uiState.value!!.copy(
                        isShowMessage = true,
                        message = it.message ?: MessageSource.ERROR
                    )
                }
            }
        }
    }

    private fun getForMeMovies() {
        viewModelScope.launch {
            getMoviesByFilterUseCase(FilterMain.FOR_ME).collect { result ->
                result.onSuccess {
                    _forMeList.value = it
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
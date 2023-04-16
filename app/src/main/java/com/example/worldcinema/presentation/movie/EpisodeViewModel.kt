package com.example.worldcinema.presentation.movie

import androidx.lifecycle.*
import com.example.worldcinema.Constants
import com.example.worldcinema.MessageSource
import com.example.worldcinema.domain.model.CollectionInfo
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
    private val calculateReleaseYearsUseCase: CalculateReleaseYearsUseCase,
    private val getCollectionsUseCase: GetCollectionsUseCase,
    private val getFilmsCollectionUseCase: GetFilmsCollectionUseCase,
    private val isFilmInFavourUseCase: IsFilmInFavourUseCase,
    private val addFilmInCollectionUseCase: AddFilmInCollectionUseCase,
    private val changeFavourFilmStatusUseCase: ChangeFavourFilmStatusUseCase,
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

    private val _releaseYears = MutableLiveData<String>()
    val releaseYears: LiveData<String> = _releaseYears

    private val _timePosition = MutableLiveData<Long>()
    val timePosition: LiveData<Long> = _timePosition

    private val _collectionList = MutableLiveData(emptyList<CollectionInfo>())
    val collectionList: LiveData<List<CollectionInfo>> =_collectionList

    private val _alreadyInFavour = MutableLiveData<Boolean>()
    val alreadyInFavour: LiveData<Boolean> = _alreadyInFavour

    private var favourId: String = ""
    private var isChangingStatus = false

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
            _releaseYears.value = args.releaseYear!!
            getCollectionList()
        }
    }

    fun saveVideoPosition(time: Long, reason: Int) {
        if (_episode.value == null) {
            return
        }
        val timeInSeconds = time / 1000
        if (timeInSeconds <= 0 || timeInSeconds == _timePosition.value) {
            setNavigation(reason)
            return
        }
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

    fun addFilmInCollection(index: Int) {
        if (_movie.value?.movieId == null) {
            return
        }
        viewModelScope.launch {
            addFilmInCollectionUseCase(
                _collectionList.value!![index].collectionId, _movie.value!!.movieId
            ).collect { result ->
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

    fun changeFilmFavourStatus() {
        if (isChangingStatus) {
            return
        }
        isChangingStatus = true
        viewModelScope.launch {
            changeFavourFilmStatusUseCase(
                favourId, _movie.value!!.movieId, alreadyInFavour.value!!
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
        _uiState.value = _uiState.value!!.copy(
            isLoadingEpisode = true
        )
        viewModelScope.launch {
            getEpisodesUseCase(movieId).collect { result ->
                result.onSuccess { episodeList ->
                    _episode.value = episodeList.find { it.episodeId == episodeId }
                    _releaseYears.value = calculateReleaseYearsUseCase(episodeList)
                    getVideoPosition(episodeId)
                    _uiState.value = _uiState.value!!.copy(
                        isLoadingEpisode = false
                    )
                }.onFailure {
                    _uiState.value = _uiState.value!!.copy(
                        isShowMessage = true,
                        message = it.message ?: MessageSource.ERROR,
                        isLoadingEpisode = false
                    )
                }
            }
        }
    }

    private fun getMovieById(movieId: String) {
        viewModelScope.launch {
            getLastViewMovieByIdUseCase(movieId).collect { result ->
                result.onSuccess {
                    _movie.value = it
                    getCollectionList()
                }.onFailure {
                    _uiState.value = _uiState.value!!.copy(
                        isShowMessage = true,
                        message = it.message ?: MessageSource.ERROR
                    )
                }
            }
        }
    }

    private fun getCollectionList() {
        viewModelScope.launch {
            getCollectionsUseCase().collect { result ->
                result.onSuccess { list ->
                    _collectionList.value = list.filter {
                        if (it.name != Constants.FAVOUR_COLLECTION)  {
                            true
                        } else {
                            isFilmInFavour(it.collectionId)
                            favourId = it.collectionId
                            false
                        }
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

    private fun isFilmInFavour(collectionId: String) {
        viewModelScope.launch {
            getFilmsCollectionUseCase(collectionId).collect { result ->
                result.onSuccess {
                    if (_movie.value?.movieId != null) {
                        _alreadyInFavour.value = isFilmInFavourUseCase(_movie.value!!.movieId, it)
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

    private fun setNavigation(reason: Int) {
        if (reason == CHAT) {
            _uiState.value = _uiState.value!!.copy(
                goToChatScreen = true,
                chatId = _movie.value!!.chatInfo!!.chatId,
                chatName = _movie.value!!.chatInfo!!.name
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
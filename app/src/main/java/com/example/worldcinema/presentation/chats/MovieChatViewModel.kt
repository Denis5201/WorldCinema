package com.example.worldcinema.presentation.chats

import androidx.lifecycle.*
import com.example.worldcinema.MessageSource
import com.example.worldcinema.domain.model.ChatMessage
import com.example.worldcinema.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieChatViewModel @Inject constructor(
    private val getUserAccountUseCase: GetUserAccountUseCase,
    private val setChatConnectionUseCase: SetChatConnectionUseCase,
    private val sendChatMessageUseCase: SendChatMessageUseCase,
    private val disconnectChatUseCase: DisconnectChatUseCase,
    private val messageSource: MessageSource,
    getUserIdUseCase: GetUserIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableLiveData(MovieChatUiState())
    val uiState: LiveData<MovieChatUiState> = _uiState

    private val _statusConnection = MutableLiveData(false)
    val statusConnection: LiveData<Boolean> = _statusConnection

    private val _cleanInput = MutableLiveData(false)
    val cleanInput: LiveData<Boolean> = _cleanInput

    var message: LiveData<ChatMessage>? = null
        private set

    private val chatId: String
    val chatName: String
    var userId: String= ""
        private set

    init {
        val args = MovieChatFragmentArgs.fromSavedStateHandle(savedStateHandle)
        chatId = args.chatId
        chatName = args.chatName
        val localUserId = getUserIdUseCase()
        if (localUserId.isNullOrEmpty()) {
            getUserId()
        } else {
            userId = localUserId
            setChatConnection()
        }
    }

    fun sendMessage(text: String) {
        if (text.isEmpty()) {
            _uiState.value = _uiState.value!!.copy(
                isShowMessage = true,
                message = messageSource.getMessage(MessageSource.SEND_EMPTY_MESSAGE)
            )
            return
        }
        viewModelScope.launch {
            sendChatMessageUseCase(text).collect { result ->
                result.onSuccess {
                    _cleanInput.value = true
                }.onFailure {
                    _uiState.value = _uiState.value!!.copy(
                        isShowMessage = true,
                        message = it.message ?: MessageSource.ERROR
                    )
                }
            }
        }
    }

    fun disconnect() {
        _statusConnection.value = false
        viewModelScope.launch {
            disconnectChatUseCase().collect {}
        }
    }

    fun setDefaultStatus() {
        _cleanInput.value = false
        _uiState.value = _uiState.value!!.copy(
            isShowMessage = false
        )
    }

    private fun getUserId() {
        viewModelScope.launch {
            getUserAccountUseCase().collect { result ->
                result.onSuccess {
                    userId = it.userId
                    setChatConnection()
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

    private fun setChatConnection() {
        viewModelScope.launch {
            setChatConnectionUseCase(chatId).collect { result ->
                result.onSuccess {
                    _uiState.value = _uiState.value!!.copy(
                        isLoading = false
                    )
                    message = it
                    _statusConnection.value = true
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
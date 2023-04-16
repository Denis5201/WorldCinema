package com.example.worldcinema.presentation.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldcinema.MessageSource
import com.example.worldcinema.domain.model.ChatInfo
import com.example.worldcinema.domain.usecase.GetUserChatListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserChatListViewModel @Inject constructor(
    private val getUserChatListUseCase: GetUserChatListUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData(UserChatListUiState())
    val uiState: LiveData<UserChatListUiState> = _uiState

    private val _chatList = MutableLiveData(emptyList<ChatInfo>())
    val chatList: LiveData<List<ChatInfo>> = _chatList

    fun getUserChatList() {
        viewModelScope.launch {
            getUserChatListUseCase().collect { result ->
                result.onSuccess {
                    _chatList.value = it
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

    fun goToChat(chatInfo: ChatInfo) {
        _uiState.value = _uiState.value!!.copy(
            chatId = chatInfo.chatId,
            chatName = chatInfo.chatName,
            goToMovieChat = true
        )
    }

    fun setDefaultStatus() {
        _uiState.value = _uiState.value!!.copy(
            isShowMessage = false,
            goToMovieChat = false
        )
    }
}
package com.example.worldcinema.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldcinema.MessageSource
import com.example.worldcinema.domain.model.Credentials
import com.example.worldcinema.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val isTokenValidYetUseCase: IsTokenValidYetUseCase,
    private val isStringsEmptyUseCase: IsStringsEmptyUseCase,
    private val isEmailFormatUseCase: IsEmailFormatUseCase,
    private val messageSource: MessageSource,
    isFirstRunAppUseCase: IsFirstRunAppUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData(SignInUiState())
    val uiState: LiveData<SignInUiState> = _uiState

    init {
        if (isFirstRunAppUseCase()) {
            _uiState.value = _uiState.value!!.copy(mustNavigateToRegister = true)
        } else {
            isTokenValid()
        }
    }

    fun signIn(email: String, password: String) {
        if (isStringsEmptyUseCase(email, password)) {
            setErrorMessage(MessageSource.EMPTY_INPUT)
            return
        }
        if (!isEmailFormatUseCase(email)) {
            setErrorMessage(MessageSource.WRONG_EMAIL_FORMAT)
            return
        }
        if (_uiState.value!!.makingRequest) {
            return
        } else {
            _uiState.value = _uiState.value!!.copy(makingRequest = true)
        }
        viewModelScope.launch {
            signInUseCase(Credentials(email, password)).collect { result ->
                result.onSuccess {
                    _uiState.value = _uiState.value!!.copy(
                        makingRequest = false,
                        mayNavigateToMain = true
                    )
                }.onFailure {
                    _uiState.value = _uiState.value!!.copy(
                        makingRequest = false,
                        isShowMessage = true,
                        message = it.message ?: MessageSource.ERROR
                    )
                }
            }
        }
    }

    fun closeDialog() {
        _uiState.value = _uiState.value!!.copy(isShowMessage = false)
    }

    fun afterNavigate() {
        _uiState.value = _uiState.value!!.copy(
            isLoading = false,
            mayNavigateToMain = false,
            mustNavigateToRegister = false
        )
    }

    private fun isTokenValid() {
        viewModelScope.launch {
            if (isTokenValidYetUseCase()) {
                _uiState.value = _uiState.value!!.copy(
                    mayNavigateToMain = true,
                )
            } else {
                _uiState.value = _uiState.value!!.copy(
                    isLoading = false,
                )
            }
        }
    }

    private fun setErrorMessage(reason: Int) {
        _uiState.value = _uiState.value!!.copy(
            isShowMessage = true,
            message = messageSource.getMessage(reason)
        )
    }
}
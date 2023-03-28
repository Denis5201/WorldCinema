package com.example.worldcinema.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldcinema.MessageSource
import com.example.worldcinema.domain.model.RegistrationForm
import com.example.worldcinema.domain.usecase.IsEmailFormatUseCase
import com.example.worldcinema.domain.usecase.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val isEmailFormatUseCase: IsEmailFormatUseCase,
    private val messageSource: MessageSource
) : ViewModel() {

    private val _uiState = MutableLiveData(SignUpUiState())
    val uiState: LiveData<SignUpUiState> = _uiState

    fun register(registrationInfo: RegistrationInfo) {
        if (isInputNotValid(registrationInfo)) {
            _uiState.value = _uiState.value!!.copy(
                isShowMessage = true
            )
            return
        }
        if (_uiState.value!!.isLoading) {
            return
        } else {
            _uiState.value = _uiState.value!!.copy(isLoading = true)
        }
        val form = RegistrationForm(
            registrationInfo.email,
            registrationInfo.password,
            registrationInfo.name,
            registrationInfo.surname
        )
        viewModelScope.launch {
            registerUserUseCase(form).collect { result ->
                result.onSuccess {
                    _uiState.value = _uiState.value!!.copy(
                        isLoading = false,
                        mayNavigateToMain = true
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

    fun closeDialog() {
        _uiState.value = _uiState.value!!.copy(isShowMessage = false)
    }

    fun afterNavigate() {
        _uiState.value = _uiState.value!!.copy(
            isLoading = false,
            mayNavigateToMain = false
        )
    }

    private fun isInputNotValid(registrationInfo: RegistrationInfo): Boolean {
        if (isInputEmpty(registrationInfo)) {
            setErrorMessage(MessageSource.EMPTY_INPUT)
            return true
        }
        if (!isEmailFormatUseCase(registrationInfo.email)) {
            setErrorMessage(MessageSource.WRONG_EMAIL_FORMAT)
            return true
        }
        if (registrationInfo.password != registrationInfo.confirmPassword) {
            setErrorMessage(MessageSource.PASSWORD_NOT_EQUAL_WITH_CONFIRM)
            return true
        }
        return false
    }

    private fun isInputEmpty(registrationInfo: RegistrationInfo): Boolean {
        return registrationInfo.name.isEmpty() || registrationInfo.surname.isEmpty()
                || registrationInfo.email.isEmpty() || registrationInfo.password.isEmpty()
                || registrationInfo.confirmPassword.isEmpty()
    }

    private fun setErrorMessage(reason: Int) {
        _uiState.value = _uiState.value!!.copy(
            message = messageSource.getMessage(reason)
        )
    }
}
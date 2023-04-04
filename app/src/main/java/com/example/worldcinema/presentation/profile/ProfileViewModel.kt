package com.example.worldcinema.presentation.profile

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldcinema.MessageSource
import com.example.worldcinema.domain.model.UserAccount
import com.example.worldcinema.domain.usecase.ClearUserDataUseCase
import com.example.worldcinema.domain.usecase.GetUserAccountUseCase
import com.example.worldcinema.domain.usecase.LoadAvatarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserAccountUseCase: GetUserAccountUseCase,
    private val clearUserDataUseCase: ClearUserDataUseCase,
    private val loadAvatarUseCase: LoadAvatarUseCase
): ViewModel() {

    private val _uiState = MutableLiveData<ProfileUiState>()
    val uiState: LiveData<ProfileUiState> = _uiState

    private val _profile = MutableLiveData<UserAccount>()
    val profile: LiveData<UserAccount> = _profile

    init {
        _uiState.value = ProfileUiState(isLoading = true)
        getAccount()
    }

    fun logout() {
        clearUserDataUseCase()
    }

    fun setDefaultStatus() {
        _uiState.value = _uiState.value!!.copy(
            isShowMessage = false,
            loadedAvatar = false
        )
    }

    private fun getAccount() {
        viewModelScope.launch {
            getUserAccountUseCase().collect { result ->
                result.onSuccess {
                    _profile.value = it
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

    fun loadAvatar(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos)

        viewModelScope.launch {
            loadAvatarUseCase(baos.toByteArray()).collect { result ->
                result.onSuccess {
                    _uiState.value = _uiState.value!!.copy(
                        loadedAvatar = true
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
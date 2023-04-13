package com.example.worldcinema.presentation.collections

import androidx.lifecycle.*
import com.example.worldcinema.MessageSource
import com.example.worldcinema.domain.model.CollectionInfo
import com.example.worldcinema.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCollectionViewModel @Inject constructor(
    private val getCollectionByStringUseCase: GetCollectionByStringUseCase,
    private val createCollectionUseCase: CreateCollectionUseCase,
    private val updateCollectionUseCase: UpdateCollectionUseCase,
    private val deleteCollectionUseCase: DeleteCollectionUseCase,
    private val isCollectionNameValidUseCase: IsCollectionNameValidUseCase,
    private val messageSource: MessageSource,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableLiveData(EditCollectionUiState())
    val uiState: LiveData<EditCollectionUiState> = _uiState

    private val _collectionIcon = MutableLiveData<String>()
    val collectionIcon: LiveData<String> = _collectionIcon

    val collection: CollectionInfo?
    val isChanging: Boolean

    init {
        val args = EditCollectionFragmentArgs.fromSavedStateHandle(savedStateHandle)
        isChanging = args.isChanging
        collection = args.collectionString?.let { getCollectionByStringUseCase(it) }
        collection?.image?.let { _collectionIcon.value = it }
    }

    fun setIcon(iconName: String) {
        if (iconName.isNotEmpty()) {
            _collectionIcon.value = iconName
        }
    }

    fun saveCollection(name: String) {
        if (name.isEmpty()) {
            setErrorMessage(MessageSource.COLLECTION_NAME_EMPTY)
            return
        }
        if (!isCollectionNameValidUseCase(name)) {
            setErrorMessage(MessageSource.CUSTOM_FAVOUR_COLLECTION)
            return
        }
        if (isChanging) {
            viewModelScope.launch {
                updateCollectionUseCase(
                    CollectionInfo(collection!!.collectionId, name, _collectionIcon.value)
                )
                _uiState.value = _uiState.value!!.copy(
                    goBackAfterSave = true
                )
            }
        } else {
            viewModelScope.launch {
                createCollectionUseCase(name, _collectionIcon.value).collect { result ->
                    result.onSuccess {
                        _uiState.value = _uiState.value!!.copy(
                            goBackAfterSave = true
                        )
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

    fun deleteCollection() {
        viewModelScope.launch {
            deleteCollectionUseCase(collection!!.collectionId).collect { result ->
                result.onSuccess {
                    _uiState.value = _uiState.value!!.copy(
                        goBackAfterSave = true
                    )
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
            isShowMessage = false,
            goBackAfterSave = false
        )
    }

    private fun setErrorMessage(reason: Int) {
        _uiState.value = _uiState.value!!.copy(
            isShowMessage = true,
            message = messageSource.getMessage(reason)
        )
    }
}
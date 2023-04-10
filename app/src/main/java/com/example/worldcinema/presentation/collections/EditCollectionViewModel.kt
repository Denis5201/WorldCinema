package com.example.worldcinema.presentation.collections

import androidx.lifecycle.*
import com.example.worldcinema.MessageSource
import com.example.worldcinema.domain.model.CollectionInfo
import com.example.worldcinema.domain.usecase.CreateCollectionUseCase
import com.example.worldcinema.domain.usecase.GetCollectionByStringUseCase
import com.example.worldcinema.domain.usecase.UpdateCollectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCollectionViewModel @Inject constructor(
    private val getCollectionByStringUseCase: GetCollectionByStringUseCase,
    private val createCollectionUseCase: CreateCollectionUseCase,
    private val updateCollectionUseCase: UpdateCollectionUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableLiveData(EditCollectionUiState())
    val uiState: LiveData<EditCollectionUiState> = _uiState

    private val _collectionIcon = MutableLiveData<String>()
    val collectionIcon: LiveData<String> = _collectionIcon

    var collection: CollectionInfo?
        private set
    val isChanging: Boolean

    init {
        val args = EditCollectionFragmentArgs.fromSavedStateHandle(savedStateHandle)
        isChanging = args.isChanging
        collection = args.collectionString?.let { getCollectionByStringUseCase(it) }
        collection?.image?.let { _collectionIcon.value = it }
    }

    fun setName(newName: String) {

    }

    fun setIcon(iconName: String) {
        _collectionIcon.value = iconName
    }

    fun saveCollection(name: String) {
        if (isChanging) {
            viewModelScope.launch {
                updateCollectionUseCase(
                    CollectionInfo(collection!!.collectionId, name, collectionIcon.value)
                )
            }
        } else {
            viewModelScope.launch {
                createCollectionUseCase(name).collect { result ->
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

    }

    fun setDefaultStatus() {
        _uiState.value = _uiState.value!!.copy(
            isShowMessage = false
        )
    }
}
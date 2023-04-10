package com.example.worldcinema.presentation.collections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.worldcinema.MessageSource
import com.example.worldcinema.domain.model.CollectionInfo
import com.example.worldcinema.domain.usecase.GetCollectionsUseCase
import com.example.worldcinema.domain.usecase.GetStringCollectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(
    private val getCollectionsUseCase: GetCollectionsUseCase,
    private val getStringCollectionUseCase: GetStringCollectionUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData(CollectionsUiState())
    val uiState: LiveData<CollectionsUiState> = _uiState

    private val _collectionList = MutableLiveData(emptyList<CollectionInfo>())
    val collectionList: LiveData<List<CollectionInfo>> = _collectionList

    init {
        getCollectionList()
    }

    fun goToFilmsCollection(collection: CollectionInfo) {
        _uiState.value = _uiState.value!!.copy(
            goToFilmsCollection = true,
            collectionString = getStringCollectionUseCase(collection)
        )
    }

    fun goToCreateCollection() {
        _uiState.value = _uiState.value!!.copy(
            goToCreateCollection = true,
        )
    }


    fun setDefaultStatus() {
        _uiState.value = _uiState.value!!.copy(
            isLoading = false,
            isRefreshing = false,
            goToFilmsCollection = false,
            goToCreateCollection = false,
            isShowMessage = false
        )
    }

    fun getCollectionList() {
        if (_uiState.value!!.isRefreshing) {
            return
        }
        _uiState.value = _uiState.value!!.copy(
            isRefreshing = true,
        )
        viewModelScope.launch {
            getCollectionsUseCase().collect { result ->
                result.onSuccess {
                    _collectionList.value = it
                    _uiState.value = _uiState.value!!.copy(
                        isLoading = false,
                        isRefreshing = false,
                    )
                }.onFailure {
                    _uiState.value = _uiState.value!!.copy(
                        isLoading = false,
                        isRefreshing = false,
                        isShowMessage = true,
                        message = it.message ?: MessageSource.ERROR
                    )
                }
            }
        }
    }
}
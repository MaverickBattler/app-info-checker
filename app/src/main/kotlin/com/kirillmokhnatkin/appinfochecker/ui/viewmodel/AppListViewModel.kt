package com.kirillmokhnatkin.appinfochecker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirillmokhnatkin.appinfochecker.domain.interactor.GetAllApplicationsInfoInteractor
import com.kirillmokhnatkin.appinfochecker.ui.mapper.AppListUiStateMapper
import com.kirillmokhnatkin.appinfochecker.ui.state.AppListUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppListViewModel(
    private val getAllApplicationsInfoInteractor: GetAllApplicationsInfoInteractor,
    private val appListUiStateMapper: AppListUiStateMapper,
): ViewModel() {

    private val _uiState: MutableStateFlow<AppListUiState> = MutableStateFlow(AppListUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadAppInfoList()
    }

    fun refreshAppList() {
        if (uiState.value != AppListUiState.Loading) {
            setSwipeRefreshLoading()
            loadAppInfoList()
        }
    }

    private fun loadAppInfoList() {
        viewModelScope.launch(Dispatchers.IO) {
            val appInfoList = getAllApplicationsInfoInteractor.getApplicationsShortInfo()
            updateUiState(appListUiStateMapper.mapAppListUiState(appInfoList))
        }
    }

    private fun setSwipeRefreshLoading() {
        _uiState.update { currentState ->
            if (currentState is AppListUiState.Content) {
                currentState.copy(isSwipeRefreshLayoutIndicatorShown = true)
            } else {
                currentState
            }
        }
    }

    private suspend fun updateUiState(uiState: AppListUiState) {
        _uiState.emit(uiState)
    }
}
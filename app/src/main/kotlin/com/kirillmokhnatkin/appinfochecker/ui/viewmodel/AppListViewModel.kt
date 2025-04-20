package com.kirillmokhnatkin.appinfochecker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirillmokhnatkin.appinfochecker.domain.interactor.GetInfoForGivenApplicationsInteractor
import com.kirillmokhnatkin.appinfochecker.domain.interactor.GetApplicationInfoListInteractor
import com.kirillmokhnatkin.appinfochecker.domain.model.AppShortInfo
import com.kirillmokhnatkin.appinfochecker.ui.mapper.AppListUiStateMapper
import com.kirillmokhnatkin.appinfochecker.ui.state.AppListUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppListViewModel(
    private val getInfoForGivenApplicationsInteractor: GetInfoForGivenApplicationsInteractor,
    private val getApplicationInfoListInteractor: GetApplicationInfoListInteractor,
    private val appListUiStateMapper: AppListUiStateMapper,
) : ViewModel() {

    private val _uiState: MutableStateFlow<AppListUiState> =
        MutableStateFlow(AppListUiState.Loading)
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
            val applicationInfoList = getApplicationInfoListInteractor.getApplicationInfoList()
            (listOf(applicationInfoList.take(FIRST_PART_SIZE)) + applicationInfoList.drop(
                FIRST_PART_SIZE
            ).chunked(APP_INFO_LOAD_PART_SIZE)).forEach { part ->
                val appShortInfoList =
                    getInfoForGivenApplicationsInteractor.getInfoForGivenApplications(part)
                addAppInfoListPart(appShortInfoList)
            }
        }
    }

    private fun addAppInfoListPart(appShortInfoList: List<AppShortInfo>) {
        _uiState.update { curState ->
            when (curState) {
                AppListUiState.Loading -> {
                    appListUiStateMapper.mapAppListUiState(emptyList(), appShortInfoList)
                }

                is AppListUiState.Content -> {
                    appListUiStateMapper.mapAppListUiState(curState.appsInfoList, appShortInfoList)
                }
            }
        }

    }

    private fun setSwipeRefreshLoading() {
        _uiState.update { currentState ->
            if (currentState is AppListUiState.Content) {
                currentState.copy(shouldHideRefreshLayoutIndicator = false)
            } else {
                currentState
            }
        }
    }

    private companion object {
        const val FIRST_PART_SIZE = 60
        const val APP_INFO_LOAD_PART_SIZE = 25
    }
}
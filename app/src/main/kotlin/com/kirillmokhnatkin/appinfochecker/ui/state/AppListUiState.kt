package com.kirillmokhnatkin.appinfochecker.ui.state

import com.kirillmokhnatkin.appinfochecker.ui.model.AppInfoModel

sealed interface AppListUiState {

    data object Loading: AppListUiState

    data class Content(
        val appsInfoList: List<AppInfoModel>,
        val isSwipeRefreshLayoutIndicatorShown: Boolean
    ): AppListUiState
}
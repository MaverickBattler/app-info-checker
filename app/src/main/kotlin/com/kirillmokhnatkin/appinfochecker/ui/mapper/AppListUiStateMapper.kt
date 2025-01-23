package com.kirillmokhnatkin.appinfochecker.ui.mapper

import android.app.Application
import com.kirillmokhnatkin.appinfochecker.R
import com.kirillmokhnatkin.appinfochecker.domain.model.AppShortInfo
import com.kirillmokhnatkin.appinfochecker.ui.model.AppInfoModel
import com.kirillmokhnatkin.appinfochecker.ui.state.AppListUiState

class AppListUiStateMapper(
    private val application: Application,
) {

    fun mapAppListUiState(appShortInfoList: List<AppShortInfo>): AppListUiState {
        val appInfoModelList = appShortInfoList
            .sortedBy { it.appName }
            .map { appShortInfo ->
                AppInfoModel(
                    appName = appShortInfo.appName,
                    packageName = appShortInfo.packageName,
                    versionName = appShortInfo.versionName
                        ?: application.getString(R.string.version_unknown)
                )
            }
        return AppListUiState.Content(
            appsInfoList = appInfoModelList,
            isSwipeRefreshLayoutIndicatorShown = false
        )
    }
}
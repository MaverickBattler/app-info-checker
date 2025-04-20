package com.kirillmokhnatkin.appinfochecker.ui.mapper

import android.app.Application
import com.kirillmokhnatkin.appinfochecker.R
import com.kirillmokhnatkin.appinfochecker.domain.model.AppShortInfo
import com.kirillmokhnatkin.appinfochecker.ui.model.AppInfoModel
import com.kirillmokhnatkin.appinfochecker.ui.state.AppListUiState

class AppListUiStateMapper(
    private val application: Application,
) {

    fun mapAppListUiState(
        curList: List<AppInfoModel>,
        appShortInfoList: List<AppShortInfo>
    ): AppListUiState {
        val appInfoModelList = appShortInfoList
            .map { appShortInfo ->
                AppInfoModel(
                    appName = appShortInfo.appName,
                    packageName = appShortInfo.packageName,
                    versionName = appShortInfo.versionName
                        ?: application.getString(R.string.version_unknown),
                )
            }
        val appsInfoList = curList + appInfoModelList
        return AppListUiState.Content(
            appsInfoList = appsInfoList,
            shouldHideRefreshLayoutIndicator = true
        )
    }
}
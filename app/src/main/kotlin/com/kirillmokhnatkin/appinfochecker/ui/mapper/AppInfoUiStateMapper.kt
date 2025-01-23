package com.kirillmokhnatkin.appinfochecker.ui.mapper

import android.app.Application
import android.text.SpannableStringBuilder
import androidx.core.text.bold
import com.kirillmokhnatkin.appinfochecker.R
import com.kirillmokhnatkin.appinfochecker.domain.model.AppInfoWithChecksum
import com.kirillmokhnatkin.appinfochecker.ui.state.AppInfoUiState

class AppInfoUiStateMapper(
    private val application: Application,
) {

    fun mapAppInfoUiState(appInfo: AppInfoWithChecksum?): AppInfoUiState {
        if (appInfo == null) {
            return AppInfoUiState.Error
        }
        val versionIsString = application.getString(R.string.version_is)
        val version = appInfo.versionName ?: application.getString(R.string.version_unknown)
        val versionString = SpannableStringBuilder().append(versionIsString).bold {
            append(version)
        }
        val stringChecksum = "%08x".format(appInfo.apkChecksum)
        return AppInfoUiState.Content(
            appName = appInfo.appName,
            packageName = appInfo.packageName,
            versionName = versionString,
            checkSum = stringChecksum,
            appIcon = appInfo.appIcon,
            buttonOpenAppEnabled = appInfo.canOpenApp
        )
    }
}
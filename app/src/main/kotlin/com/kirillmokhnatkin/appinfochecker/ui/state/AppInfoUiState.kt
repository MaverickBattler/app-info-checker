package com.kirillmokhnatkin.appinfochecker.ui.state

import android.graphics.drawable.Drawable

sealed interface AppInfoUiState {

    data object Loading: AppInfoUiState

    data class Content(
        val appName: String,
        val packageName: String,
        val versionName: CharSequence,
        val checkSum: String,
        val appIcon: Drawable,
    ): AppInfoUiState

    data object Error: AppInfoUiState
}

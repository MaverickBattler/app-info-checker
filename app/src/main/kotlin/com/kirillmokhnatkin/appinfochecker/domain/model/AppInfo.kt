package com.kirillmokhnatkin.appinfochecker.domain.model

import android.graphics.drawable.Drawable

data class AppInfo(
    val appName: String,
    val packageName: String,
    val versionName: String?,
    val apkPath: String,
    val appIcon: Drawable,
)
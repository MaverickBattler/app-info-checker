package com.kirillmokhnatkin.appinfochecker.domain.model

import android.graphics.drawable.Drawable

class AppInfoWithChecksum(
    val appName: String,
    val packageName: String,
    val versionName: String?,
    val apkChecksum: Long,
    val appIcon: Drawable,
)
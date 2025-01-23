package com.kirillmokhnatkin.appinfochecker.domain.model

data class AppShortInfo(
    val appName: String,
    val packageName: String,
    val versionName: String?,
)
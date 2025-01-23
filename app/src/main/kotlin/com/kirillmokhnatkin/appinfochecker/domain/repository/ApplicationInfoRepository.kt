package com.kirillmokhnatkin.appinfochecker.domain.repository

import com.kirillmokhnatkin.appinfochecker.domain.model.AppInfo

interface ApplicationInfoRepository {

    suspend fun getApplicationInfo(appPackage: String): AppInfo?
}
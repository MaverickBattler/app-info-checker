package com.kirillmokhnatkin.appinfochecker.domain.repository

import com.kirillmokhnatkin.appinfochecker.domain.model.AppShortInfo

interface ApplicationsInfoRepository {

    suspend fun getApplicationsInfo(): List<AppShortInfo>
}
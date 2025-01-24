package com.kirillmokhnatkin.appinfochecker.domain.repository

import android.content.pm.ApplicationInfo
import com.kirillmokhnatkin.appinfochecker.domain.model.AppShortInfo

interface ApplicationsInfoRepository {

    suspend fun getApplicationInfoList(): List<ApplicationInfo>

    suspend fun getInfoForGivenApplications(applicationsInfo: List<ApplicationInfo>): List<AppShortInfo>
}
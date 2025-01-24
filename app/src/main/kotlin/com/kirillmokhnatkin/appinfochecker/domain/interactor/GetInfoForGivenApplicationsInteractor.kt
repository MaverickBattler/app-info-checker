package com.kirillmokhnatkin.appinfochecker.domain.interactor

import android.content.pm.ApplicationInfo
import com.kirillmokhnatkin.appinfochecker.domain.model.AppShortInfo
import com.kirillmokhnatkin.appinfochecker.domain.repository.ApplicationsInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetInfoForGivenApplicationsInteractor(
    private val applicationsInfoRepository: ApplicationsInfoRepository
) {

    suspend fun getInfoForGivenApplications(
        applicationsInfo: List<ApplicationInfo>
    ): List<AppShortInfo> {
        return withContext(Dispatchers.IO) {
            applicationsInfoRepository.getInfoForGivenApplications(applicationsInfo)
        }
    }
}
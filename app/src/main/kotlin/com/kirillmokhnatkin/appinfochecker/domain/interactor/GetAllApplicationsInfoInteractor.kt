package com.kirillmokhnatkin.appinfochecker.domain.interactor

import com.kirillmokhnatkin.appinfochecker.domain.model.AppShortInfo
import com.kirillmokhnatkin.appinfochecker.domain.repository.ApplicationsInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetAllApplicationsInfoInteractor(
    private val applicationsInfoRepository: ApplicationsInfoRepository
) {

    suspend fun getApplicationsShortInfo(): List<AppShortInfo> {
        return withContext(Dispatchers.IO) {
            applicationsInfoRepository.getApplicationsInfo()
        }
    }
}
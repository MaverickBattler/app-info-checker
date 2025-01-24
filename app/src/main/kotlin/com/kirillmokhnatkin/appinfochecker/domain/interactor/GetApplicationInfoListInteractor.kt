package com.kirillmokhnatkin.appinfochecker.domain.interactor

import android.content.pm.ApplicationInfo
import com.kirillmokhnatkin.appinfochecker.domain.repository.ApplicationsInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetApplicationInfoListInteractor(
    private val applicationsInfoRepository: ApplicationsInfoRepository,
) {

    suspend fun getApplicationInfoList(): List<ApplicationInfo> {
        return withContext(Dispatchers.IO) {
            applicationsInfoRepository.getApplicationInfoList()
        }
    }
}
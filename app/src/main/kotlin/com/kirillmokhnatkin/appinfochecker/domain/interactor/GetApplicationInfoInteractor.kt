package com.kirillmokhnatkin.appinfochecker.domain.interactor

import com.kirillmokhnatkin.appinfochecker.domain.model.AppInfoWithChecksum
import com.kirillmokhnatkin.appinfochecker.domain.repository.ApplicationInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetApplicationInfoInteractor(
    private val applicationInfoRepository: ApplicationInfoRepository
) {

    suspend fun getApplicationInfo(appPackage: String): AppInfoWithChecksum? {
        return withContext(Dispatchers.IO) {
            val appInfo =
                applicationInfoRepository.getApplicationInfo(appPackage) ?: return@withContext null
            val checksum = calculateApkCheckSum(appInfo.apkPath)
            AppInfoWithChecksum(
                appName = appInfo.appName,
                packageName = appInfo.packageName,
                versionName = appInfo.versionName,
                apkChecksum = checksum,
                appIcon = appInfo.appIcon,
                canOpenApp = appInfo.canOpenApp,
            )
        }
    }

    private external fun calculateApkCheckSum(apkFilePath: String): Long

}
package com.kirillmokhnatkin.appinfochecker.data

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import com.kirillmokhnatkin.appinfochecker.domain.repository.ApplicationsInfoRepository
import com.kirillmokhnatkin.appinfochecker.domain.model.AppShortInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApplicationsInfoRepositoryImpl(
    private val application: Application
) : ApplicationsInfoRepository {

    override suspend fun getApplicationInfoList(): List<ApplicationInfo> {
        return withContext(Dispatchers.IO) {
            val packageManager = application.packageManager
            packageManager.getInstalledApplications(0)
        }
    }

    override suspend fun getInfoForGivenApplications(
        applicationsInfo: List<ApplicationInfo>
    ): List<AppShortInfo> {
        return withContext(Dispatchers.IO) {
            val packageManager = application.packageManager
            val apps = mutableListOf<AppShortInfo>()
            for (applicationInfo in applicationsInfo) {
                apps.add(getAppShortInfo(packageManager, applicationInfo))
            }
            apps
        }
    }

    private fun getAppShortInfo(
        packageManager: PackageManager,
        applicationInfo: ApplicationInfo
    ): AppShortInfo {
        val appLabel = packageManager.getApplicationLabel(applicationInfo).toString()
        val packageName = applicationInfo.packageName
        val packageInfo: PackageInfo? = try {
            packageManager.getPackageInfo(packageName, 0)
        } catch (e: NameNotFoundException) {
            null // found app info, but didn't find package info
        }
        val versionName = packageInfo?.versionName
        return AppShortInfo(
            appName = appLabel,
            packageName = packageName,
            versionName = versionName,
        )
    }
}
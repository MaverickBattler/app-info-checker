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
import timber.log.Timber

class ApplicationsInfoRepositoryImpl(
    private val application: Application
) : ApplicationsInfoRepository {

    override suspend fun getApplicationsInfo(): List<AppShortInfo> {
        return withContext(Dispatchers.IO) {
            val startTime = System.currentTimeMillis()
            val packageManager = application.packageManager
            val apps = mutableListOf<AppShortInfo>()
            val installedApplicationsInfo = packageManager.getInstalledApplications(0)
            for (applicationInfo in installedApplicationsInfo) {
                apps.add(getAppShortInfo(packageManager, applicationInfo))
            }
            val endTime = System.currentTimeMillis()
            Timber.i("abcdea time took = ${endTime - startTime} ms")
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
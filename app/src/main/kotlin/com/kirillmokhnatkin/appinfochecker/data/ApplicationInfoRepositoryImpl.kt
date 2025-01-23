package com.kirillmokhnatkin.appinfochecker.data

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import com.kirillmokhnatkin.appinfochecker.domain.repository.ApplicationInfoRepository
import com.kirillmokhnatkin.appinfochecker.domain.model.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApplicationInfoRepositoryImpl(
    private val application: Application
): ApplicationInfoRepository {

    override suspend fun getApplicationInfo(appPackage: String): AppInfo? {
        return withContext(Dispatchers.IO) {
            val packageManager = application.packageManager
            try {
                val appInfo = packageManager.getApplicationInfo(appPackage, 0)
                val packageInfo = packageManager.getPackageInfo(appPackage, 0)
                return@withContext getAppInfo(packageManager, packageInfo, appInfo)
            } catch (e: NameNotFoundException) {
                null // Return null if package was not found
            }
        }
    }

    private fun getAppInfo(
        packageManager: PackageManager,
        packageInfo: PackageInfo,
        applicationInfo: ApplicationInfo
    ): AppInfo {
        val appLabel = packageManager.getApplicationLabel(applicationInfo).toString()
        val packageName = applicationInfo.packageName
        val versionName = packageInfo.versionName
        val apkPath = applicationInfo.sourceDir
        val appIcon = applicationInfo.loadIcon(packageManager)
        val canOpenApp = packageManager.getLaunchIntentForPackage(packageName) != null
        return AppInfo(
            appName = appLabel,
            packageName = packageName,
            versionName = versionName,
            apkPath = apkPath,
            appIcon = appIcon,
            canOpenApp = canOpenApp
        )
    }
}
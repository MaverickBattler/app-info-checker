package com.kirillmokhnatkin.appinfochecker.app

import android.app.Application
import com.kirillmokhnatkin.appinfochecker.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import com.kirillmokhnatkin.appinfochecker.BuildConfig
import timber.log.Timber

class AppInfoCheckerApp: Application() {

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        initKoin()
        System.loadLibrary("apkSrcCheckLibrary")
        super.onCreate()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@AppInfoCheckerApp)
            modules(listOf(mainModule))
        }
    }
}
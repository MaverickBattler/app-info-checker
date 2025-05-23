package com.kirillmokhnatkin.appinfochecker.di

import com.kirillmokhnatkin.appinfochecker.data.ApplicationInfoRepositoryImpl
import com.kirillmokhnatkin.appinfochecker.data.ApplicationsInfoRepositoryImpl
import com.kirillmokhnatkin.appinfochecker.domain.repository.ApplicationInfoRepository
import com.kirillmokhnatkin.appinfochecker.domain.repository.ApplicationsInfoRepository
import com.kirillmokhnatkin.appinfochecker.domain.interactor.GetInfoForGivenApplicationsInteractor
import com.kirillmokhnatkin.appinfochecker.domain.interactor.GetApplicationInfoInteractor
import com.kirillmokhnatkin.appinfochecker.domain.interactor.GetApplicationInfoListInteractor
import com.kirillmokhnatkin.appinfochecker.ui.mapper.AppInfoUiStateMapper
import com.kirillmokhnatkin.appinfochecker.ui.mapper.AppListUiStateMapper
import com.kirillmokhnatkin.appinfochecker.ui.viewmodel.AppInfoViewModel
import com.kirillmokhnatkin.appinfochecker.ui.viewmodel.AppListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {

    viewModel { parameters ->
        AppInfoViewModel(
            appPackage = parameters[0],
            getApplicationInfoInteractor = get(),
            appInfoUiStateMapper = get(),
        )
    }

    viewModel {
        AppListViewModel(
            getInfoForGivenApplicationsInteractor = get(),
            appListUiStateMapper = get(),
            getApplicationInfoListInteractor = get(),
        )
    }

    factory {
        ApplicationsInfoRepositoryImpl(
            application = androidApplication()
        )
    }

    factory<ApplicationsInfoRepository> {
        get<ApplicationsInfoRepositoryImpl>()
    }

    factory {
        ApplicationInfoRepositoryImpl(
            application = androidApplication()
        )
    }

    factory<ApplicationInfoRepository> {
        get<ApplicationInfoRepositoryImpl>()
    }

    factory {
        GetInfoForGivenApplicationsInteractor(
            applicationsInfoRepository = get()
        )
    }

    factory {
        GetApplicationInfoInteractor(
            applicationInfoRepository = get()
        )
    }

    factory {
        GetApplicationInfoListInteractor(
            applicationsInfoRepository = get()
        )
    }

    factory {
        AppListUiStateMapper(
            application = get()
        )
    }

    factory {
        AppInfoUiStateMapper(
            application = get()
        )
    }
}
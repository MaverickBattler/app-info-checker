package com.kirillmokhnatkin.appinfochecker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirillmokhnatkin.appinfochecker.domain.interactor.GetApplicationInfoInteractor
import com.kirillmokhnatkin.appinfochecker.ui.mapper.AppInfoUiStateMapper
import com.kirillmokhnatkin.appinfochecker.ui.state.AppInfoUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppInfoViewModel(
    private val appPackage: String,
    private val getApplicationInfoInteractor: GetApplicationInfoInteractor,
    private val appInfoUiStateMapper: AppInfoUiStateMapper,
): ViewModel() {

    private val _uiState: MutableStateFlow<AppInfoUiState> = MutableStateFlow(AppInfoUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getApplicationInfoAndUpdateUi()
        }
    }

    private suspend fun getApplicationInfoAndUpdateUi() {
        val appInfo = getApplicationInfoInteractor.getApplicationInfo(appPackage)
        _uiState.update {
            appInfoUiStateMapper.mapAppInfoUiState(appInfo)
        }
    }
}
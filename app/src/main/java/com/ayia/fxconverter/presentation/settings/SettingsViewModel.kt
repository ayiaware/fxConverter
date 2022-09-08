package com.ayia.fxconverter.presentation.settings


import androidx.lifecycle.*
import com.ayia.fxconverter.data.AppRepository
import com.ayia.fxconverter.domain.models.Info
import com.ayia.fxconverter.util.Theme
import com.ayia.fxconverter.util.UserPreferencesRepository
import com.ayia.fxconverter.util.BASE_TAG
import com.ayia.fxconverter.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPrefs: UserPreferencesRepository,
    private val appRepository: AppRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val myTag: String = BASE_TAG + "-"+ SettingsViewModel::class.java.simpleName

    val themePosObs = MutableLiveData(initThemePos())

    var themePos : Int = 0

    private var baseCurrency: Currency = userPrefs.currency

    val appCurrency: MutableLiveData<Currency> = MutableLiveData(baseCurrency)

    val infoObs: LiveData<Info?> = appCurrency.switchMap {
        appRepository.getInfoObs(it.currencyCode)
    }

    private fun initThemePos(): Int {

        themePos = when (userPrefs.appTheme) {
            Theme.LIGHT_MODE -> 0
            Theme.DARK_MODE -> 1
            else -> 2
        }

        return themePos

    }



   fun setTheme() {

        Timber.tag(myTag).d("Method setTheme")

        themePosObs.value = themePos

        userPrefs.updateTheme(
            when (themePos) {
                0 -> Theme.LIGHT_MODE
                1 -> Theme.DARK_MODE
                else -> Theme.FOLLOW_SYSTEM
            }
        )
    }


    fun clearData(){
        viewModelScope.launch(dispatcherProvider.io) {
            appRepository.clearDatabase()
        }
    }



}
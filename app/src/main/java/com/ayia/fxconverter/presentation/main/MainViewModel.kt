package com.ayia.fxconverter.presentation.main

import android.text.TextUtils
import androidx.lifecycle.*
import com.ayia.fxconverter.data.AppRepository
import com.ayia.fxconverter.data.Resource
import com.ayia.fxconverter.domain.models.Info
import com.ayia.fxconverter.domain.models.Option
import com.ayia.fxconverter.domain.models.Rate
import com.ayia.fxconverter.util.BASE_TAG
import com.ayia.fxconverter.util.DispatcherProvider
import com.ayia.fxconverter.util.UserPreferencesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class MainViewModel @AssistedInject constructor(
    private val repository: AppRepository,
    userPrefs: UserPreferencesRepository,
    private val dispatcher: DispatcherProvider,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val TAG = BASE_TAG + "-" + MainViewModel::class.simpleName

    private var baseCurrency: Currency = userPrefs.currency

    private val _infoAndRatesEvent = MutableStateFlow<Resource<List<Rate>>>(Resource.Loading)
    val infoAndRatesEvent: StateFlow<Resource<List<Rate>>> = _infoAndRatesEvent

    val shrinkFab = MutableLiveData(false)

    val appCurrency: MutableLiveData<Currency> = MutableLiveData(baseCurrency)

    val infoObs: LiveData<Info?> = appCurrency.switchMap {
        repository.getInfoObs(it.currencyCode)
    }

    init {
        Timber.tag(TAG).d("init")
    }

    val infoAndRatesObs: LiveData<List<Rate>> =
        savedStateHandle.getLiveData("search_rates", "").switchMap { searchQuery ->

            Timber.tag(TAG).d("searchQuery switch")

            appCurrency.switchMap { currency ->

                Timber.tag(TAG).d("appCurrency switch")

                baseCurrency = currency

                viewModelScope.launch {

                    Timber.tag(TAG).d("coroutine launch")

                    val rateInfo = repository.getInfo(currency.currencyCode)

                    if (rateInfo == null)
                        refreshInfoAndRatesObs(isCachedEmpty = true)
                    else if (rateInfo.isNextUpdateHere())
                        refreshInfoAndRatesObs(false)

                }

                if (TextUtils.isEmpty(searchQuery)) {
                    repository.getRatesObs(baseCurrency.currencyCode)
                }
                else {
                    repository.searchGetRatesObs(baseCurrency.currencyCode, searchQuery)
                    }
                }

        }

    fun refreshInfoAndRatesObs(isCachedEmpty: Boolean) {

        Timber.tag(TAG).d("refreshInfoAndRatesObs()")

        viewModelScope.launch(dispatcher.io) {
            _infoAndRatesEvent.value =
                repository.getInfoAndRates(isCachedEmpty, baseCurrency.currencyCode, isObs = true)
        }

    }

    val toCurrencyOption = MutableLiveData<Option?>()

    val fromCurrencyOption = MutableLiveData(
        Option(
            name = "${baseCurrency.displayName} (${baseCurrency.currencyCode})",
            value = baseCurrency.currencyCode
        )
    )

    fun resetOptions() {

        fromCurrencyOption.value = Option(
            name = "${baseCurrency.displayName} (${baseCurrency.currencyCode})",
            value = baseCurrency.currencyCode
        )
        toCurrencyOption.value = null

    }

    fun setSearchRatesQuery(query: String) {

        savedStateHandle["search_rates"] = query

    }


    @AssistedFactory
    interface MainAssistedFactory {
        fun create(savedStateHandle: SavedStateHandle) : MainViewModel
    }


}
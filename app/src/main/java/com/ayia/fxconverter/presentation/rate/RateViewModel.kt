package com.ayia.fxconverter.presentation.rate

import androidx.lifecycle.*
import com.ayia.fxconverter.data.AppRepository
import com.ayia.fxconverter.domain.models.Info
import com.ayia.fxconverter.domain.models.Rate
import com.ayia.fxconverter.util.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.util.*


class RateViewModel @AssistedInject constructor(
    repository: AppRepository,
    @Assisted("code") code: String,
    @Assisted("baseCode") baseCode: String
) : ViewModel() {

    private val TAG = BASE_TAG + " " + RateViewModel::class.simpleName

    init {
        Timber.tag(TAG).d("code $code baseCode $baseCode")

    }

    val rate : LiveData<Rate?>  = repository.getRateObs(code, baseCode)

    val info : LiveData<Info?> = repository.getInfoObs(baseCode)

    val currency : LiveData<Currency> = rate.switchMap {
        return@switchMap MutableLiveData(Currency.getInstance(it?.code))
    }

    val baseCurrency : LiveData<Currency> = info.switchMap {
        return@switchMap MutableLiveData(Currency.getInstance(it?.baseCode))
    }

    companion object {
        fun provideFactory(
            assistedFactory: CurrencyAssistedFactory,
            code: String,
            baseCode: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(code, baseCode) as T
            }
        }
    }

    @AssistedFactory
    interface CurrencyAssistedFactory {
        fun create(@Assisted("code") code: String, @Assisted("baseCode") baseCode: String) : RateViewModel
    }

}
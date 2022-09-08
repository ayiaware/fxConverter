package com.ayia.fxconverter.presentation.converter

import androidx.lifecycle.*
import com.ayia.fxconverter.R
import com.ayia.fxconverter.data.AppRepository
import com.ayia.fxconverter.data.Resource
import com.ayia.fxconverter.domain.models.Option
import com.ayia.fxconverter.domain.models.Rate
import com.ayia.fxconverter.util.UserPreferencesRepository
import com.ayia.fxconverter.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val repository: AppRepository,
    userPrefs: UserPreferencesRepository,
    private val dispatcher: DispatcherProvider,
) : ViewModel() {

    private val TAG = BASE_TAG + " " + ConverterViewModel::class.simpleName

    private var baseCurrency: Currency = userPrefs.currency

    private val _conversion = MutableStateFlow<Resource<String>>(Resource.Empty(R.string.msg_data_empty))
    val conversion: StateFlow<Resource<String>> = _conversion

    private val _validForm = MediatorLiveData<Boolean>()
    val validForm: LiveData<Boolean> = _validForm




    val fromCurrencyOption = MutableLiveData(
        Option(
            name = "${baseCurrency.displayName} (${baseCurrency.currencyCode})",
            value = baseCurrency.currencyCode
        )
    )

    val toCurrencyOption = MutableLiveData<Option?>()

    val amount = MutableLiveData<String?>()

    private var formState = FormState(baseCurrency.currencyCode)

    init {
        addSources()
    }


    private fun addSources() {
        _validForm.addSource(fromCurrencyOption) { validateForm() }
        _validForm.addSource(toCurrencyOption) { validateForm() }
        _validForm.addSource(amount) { validateForm() }
    }

    override fun onCleared() {
        removeSources()
        super.onCleared()
    }

    private fun removeSources() {
        _validForm.removeSource(fromCurrencyOption)
        _validForm.removeSource(toCurrencyOption)
        _validForm.removeSource(amount)
    }

    private fun validateForm() {

        val amount = amount.value

        val toCurrency = toCurrencyOption.value?.value

        val fromCurrency = fromCurrencyOption.value!!.value

        val isValidForm = isValidAmount(amount).isValid == true && isValidCurrency(
            fromCurrency,
            toCurrency
        ).isValid == true

        Timber.tag("Validator").d("isValid $isValidForm")

        _validForm.value = isValidForm && formState.isChanged(FormState(fromCurrency, toCurrency, amount))

    }


    fun convert() {

        _conversion.value = Resource.Loading

        val fromAmount = amount.value!!.toFloat()

        val toCurrency = toCurrencyOption.value!!.value

        val fromCurrency = fromCurrencyOption.value!!.value

        viewModelScope.launch(dispatcher.io) {

            val rateInfo = repository.getInfo(fromCurrency)

            val response: Resource<List<Rate>> = if (rateInfo == null) {
                repository.getInfoAndRates(isCachedEmpty = true, fromCurrency, isObs = false)
            } else if (rateInfo.isNextUpdateHere()) {
                repository.getInfoAndRates(isCachedEmpty = false, fromCurrency, isObs = false)
            } else{
                Resource.Success(repository.getRates(fromCurrency))
            }

            when (response) {

                is Resource.Error -> {
                    _conversion.value = Resource.Error(R.string.msg_data_empty)
                }
                is Resource.Empty -> {
                    _conversion.value = Resource.Error(R.string.msg_data_empty)
                }
                is Resource.Success -> {

                    val rate = getConversionRate(toCurrency, response.data)

                    Timber.tag(TAG).d("Rate $rate")

                    if (rate == null) {
                        _conversion.value = Resource.Error(R.string.msg_unexpected_error)
                    } else {
                        val convertedCurrency = (fromAmount * rate * 100) / 100
                        _conversion.value = Resource.Success(
                            "$fromAmount ${fromCurrencyOption.value!!.value} = $convertedCurrency ${toCurrencyOption.value!!.value}"
                        )
                        Timber.tag(TAG).d("Converted currency $convertedCurrency")
                    }
                }
                else -> {
                    _conversion.value = Resource.Loading
                }
            }
        }
    }

    private fun getConversionRate(currency: String, rates: List<Rate>): Double? {

        return rates.find { it.code == currency }?.amount

    }
     fun resetButton(){

        formState = FormState(fromCurrencyOption.value!!.value, toCurrencyOption.value?.value, amount.value)

        _validForm.value = false
    }


}
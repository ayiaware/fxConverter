package com.ayia.fxconverter.presentation.converter

import com.ayia.fxconverter.R
import timber.log.Timber
import java.math.BigDecimal





fun isValidCurrency(from: String, to: String?): ValidatorState {

    Timber.tag("Validator").d("From $from To $to")

    if (to.isNullOrEmpty())
       return ValidatorState()

    return if (from != to)
        ValidatorState(isValid = true)
    else
        ValidatorState(errorTxtId = R.string.error_same_currency)
}


fun isValidAmount(text: String?): ValidatorState {

    Timber.tag("Validator").d(text)

    if (text.isNullOrEmpty())
        return ValidatorState()

    return if (text != "0" && text[0] != '.' && text[text.length - 1] != '.') {

        if (text.contains(".") && text.substring(text.indexOf('.') + 1).length > 2)
            ValidatorState(errorTxtId = R.string.error_amount)
        else {
            if (areValidDigits(BigDecimal(text)))
                ValidatorState(isValid = true)
            else
                ValidatorState(errorTxtId = R.string.error_amount)
        }

    } else
        ValidatorState(errorTxtId = R.string.error_amount)


}

private fun areValidDigits(digits: BigDecimal): Boolean {
    return digits > BigDecimal.ZERO &&
            digits <= BigDecimal.valueOf(999999999999.99)
}


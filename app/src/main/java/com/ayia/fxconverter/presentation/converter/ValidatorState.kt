package com.ayia.fxconverter.presentation.converter

data class ValidatorState(var errorTxtId : Int? = null, var isValid: Boolean? = null) {
    fun isDefault() : Boolean = errorTxtId == null && isValid == null
}

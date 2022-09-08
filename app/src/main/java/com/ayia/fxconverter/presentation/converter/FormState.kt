package com.ayia.fxconverter.presentation.converter

data class FormState(var from: String, var to: String? = null, var amount: String? = null) {
    fun isChanged(formState: FormState): Boolean =
        formState.from != from || formState.to != to || formState.amount != amount
}

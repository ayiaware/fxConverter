package com.ayia.fxconverter.domain.models

import com.ayia.fxconverter.util.BASE_URL_FLAGS
import java.util.*

class Rate (
    val code: String,
    val amount: Double,
    val infoBaseCode: String,
    val isFavorite: Boolean = false,
    val isPinned : Boolean = false
){
    val name: String = Currency.getInstance(code).displayName
    fun getFlagUrl():String = BASE_URL_FLAGS + code.substring(0,2).lowercase()
}
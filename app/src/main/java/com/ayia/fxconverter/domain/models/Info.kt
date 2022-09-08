package com.ayia.fxconverter.domain.models

import com.ayia.fxconverter.util.BASE_URL_FLAGS
import com.ayia.fxconverter.util.formatDateToLong
import com.ayia.fxconverter.util.formatDateToString


data class Info(
    val baseCode: String,
    val lastUpdateUnix: Int,
    val lastUpdateUtc: String,
    val nextUpdateUnix: Int,
    val nextUpdateUtc: String
)
{
    fun updatedStr():String = formatDateToString(lastUpdateUtc)
    fun nextUpdateStr():String = formatDateToString(nextUpdateUtc)
    fun isNextUpdateHere():Boolean = formatDateToLong(nextUpdateUtc) <= System.currentTimeMillis()
    fun getFlagUrl():String = BASE_URL_FLAGS + baseCode.substring(0,2).lowercase()

}
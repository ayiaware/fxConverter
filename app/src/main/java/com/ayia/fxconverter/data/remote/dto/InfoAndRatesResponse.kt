package com.ayia.fxconverter.data.remote.dto

import com.ayia.fxconverter.domain.models.Rate


data class InfoAndRatesResponse(
    val baseCode: String,
    val rates: List<Rate>,
    val result: String,
    val timeLastUpdateUnix: Int,
    val timeLastUpdateUtc: String,
    val timeNextUpdateUnix: Int,
    val timeNextUpdateUtc: String
)
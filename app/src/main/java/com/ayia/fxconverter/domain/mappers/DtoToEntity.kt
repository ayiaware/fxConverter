package com.ayia.fxconverter.domain.mappers

import com.ayia.fxconverter.data.local.entities.RateEntity
import com.ayia.fxconverter.data.local.entities.InfoAndRatesEntity
import com.ayia.fxconverter.data.local.entities.InfoEntity
import com.ayia.fxconverter.data.remote.dto.InfoAndRatesResponse
import com.ayia.fxconverter.domain.models.Rate
import java.util.*

fun InfoAndRatesResponse.toEntity(): InfoAndRatesEntity {
    return InfoAndRatesEntity(
        InfoEntity(
            baseCode = baseCode,
            lastUpdateUnix = timeLastUpdateUnix,
            lastUpdateUtc = timeLastUpdateUtc,
            nextUpdateUnix = timeNextUpdateUnix,
            nextUpdateUtc = timeNextUpdateUtc
        ),
        rates = rates.map { it.toDomain() }
    )
}

fun Rate.toDomain(): RateEntity {
    return RateEntity(
        code, amount, infoBaseCode, Currency.getInstance(code).displayName
    )
}

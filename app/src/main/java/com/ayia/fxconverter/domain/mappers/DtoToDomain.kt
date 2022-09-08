package com.ayia.fxconverter.domain.mappers


import com.ayia.fxconverter.data.remote.dto.InfoAndRatesResponse
import com.ayia.fxconverter.domain.models.Info
import com.ayia.fxconverter.domain.models.InfoAndRates


fun InfoAndRatesResponse.toDomain(): InfoAndRates {
    return InfoAndRates(
        Info(
            baseCode = baseCode,
            lastUpdateUnix = timeLastUpdateUnix,
            lastUpdateUtc = timeLastUpdateUtc,
            nextUpdateUnix = timeNextUpdateUnix,
            nextUpdateUtc = timeNextUpdateUtc
        ),
        rates = rates
    )
}


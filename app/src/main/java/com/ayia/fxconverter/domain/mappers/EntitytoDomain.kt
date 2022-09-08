package com.ayia.fxconverter.domain.mappers

import com.ayia.fxconverter.data.local.entities.RateEntity
import com.ayia.fxconverter.data.local.entities.InfoAndRatesEntity
import com.ayia.fxconverter.data.local.entities.InfoEntity
import com.ayia.fxconverter.domain.models.Rate
import com.ayia.fxconverter.domain.models.Info
import com.ayia.fxconverter.domain.models.InfoAndRates

fun RateEntity.toDomain(): Rate {
    return Rate(
        code, amount, infoBaseCode
    )
}

fun InfoEntity.toDomain(): Info {
    return Info(
        baseCode, lastUpdateUnix, lastUpdateUtc, nextUpdateUnix, nextUpdateUtc
    )
}




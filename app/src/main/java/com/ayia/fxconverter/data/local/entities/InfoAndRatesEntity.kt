package com.ayia.fxconverter.data.local.entities

import androidx.room.*

data class InfoAndRatesEntity(
    @Embedded val info: InfoEntity,
    @Relation(parentColumn = "base_code", entityColumn = "info_base_code")
    val rates: List<RateEntity>
)



package com.ayia.fxconverter.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rates_info")
data class InfoEntity(
    @PrimaryKey
    @ColumnInfo(name = "base_code")
    val baseCode: String,
    @ColumnInfo(name = "time_last_update_unix")
    val lastUpdateUnix: Int,
    @ColumnInfo(name = "time_last_update_utc")
    val lastUpdateUtc: String,
    @ColumnInfo(name = "time_next_update_unix")
    val nextUpdateUnix: Int,
    @ColumnInfo(name = "time_next_update_utc")
    val nextUpdateUtc: String
)
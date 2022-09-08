package com.ayia.fxconverter.data.local.entities

import androidx.room.*

@Entity(
    primaryKeys = ["code" , "info_base_code"],
    tableName = "rates",
    foreignKeys = [ForeignKey(
        entity = InfoEntity::class,
        parentColumns = ["base_code"],
        childColumns = ["info_base_code"],
        onDelete = ForeignKey.CASCADE
    )
    ],
    indices = [Index(value = ["info_base_code"])]
)

data class RateEntity (
    val code: String,
    val amount: Double,
    @ColumnInfo(name = "info_base_code")
    val infoBaseCode :String,
    val name: String,
    val isFavorite: Boolean = false,
    val isPinned : Boolean = false
)
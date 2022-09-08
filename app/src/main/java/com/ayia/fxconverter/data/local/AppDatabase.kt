package com.ayia.fxconverter.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ayia.fxconverter.data.local.entities.InfoEntity
import com.ayia.fxconverter.data.local.entities.RateEntity

@Database(
    entities = [RateEntity::class, InfoEntity::class], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase(){

    abstract val appDao : AppDao

}
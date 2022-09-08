package com.ayia.fxconverter.data.local

import androidx.lifecycle.LiveData
import com.ayia.fxconverter.data.local.entities.InfoAndRatesEntity
import com.ayia.fxconverter.data.local.entities.InfoEntity
import com.ayia.fxconverter.data.local.entities.RateEntity

class LocalRepository(private val appDao: AppDao) {

    fun getRatesObs(baseCode: String) = appDao.getRatesObs(baseCode)

    fun getRates(baseCode: String) = appDao.getRates(baseCode)

    fun searchGetRatesObs(baseCode: String, searchQuery : String) = appDao.searchGetRatesObs(baseCode, searchQuery)

    suspend fun getInfo(base: String): InfoEntity? {
        return appDao.getInfo(base)
    }

    fun getInfoObs(base: String): LiveData<InfoEntity?> {
        return appDao.getInfoObs(base)
    }

    fun getRateObs(code: String, baseCode: String): LiveData<RateEntity?> {
        return appDao.getRateObs(code, baseCode)
    }

    suspend fun insertInfoAndRates(infoAndRatesEntity: InfoAndRatesEntity) {
        appDao.insertInfoAndRates(infoAndRatesEntity)
    }

    suspend fun clearDatabase() = appDao.clearDatabase()

}
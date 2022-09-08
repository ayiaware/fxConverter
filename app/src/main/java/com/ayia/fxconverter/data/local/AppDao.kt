package com.ayia.fxconverter.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ayia.fxconverter.data.local.entities.InfoAndRatesEntity
import com.ayia.fxconverter.data.local.entities.InfoEntity
import com.ayia.fxconverter.data.local.entities.RateEntity

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates : List<RateEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInfo(ratesInfo: InfoEntity)

    @Query("select * from rates where code=:code and info_base_code = :baseCode limit 1")
    fun getRateObs(code: String, baseCode: String): LiveData<RateEntity?>

    @Query("SELECT * FROM rates_info WHERE base_code = :baseCode")
    suspend fun getInfo(baseCode: String): InfoEntity?

    @Query("SELECT * FROM rates_info WHERE base_code = :baseCode")
    fun getInfoObs(baseCode: String): LiveData<InfoEntity?>

    //Automatically deletes rates
    @Query("delete from rates_info where base_code=:baseCode")
    suspend fun deleteInfo(baseCode: String)

    @Transaction
    suspend fun insertInfoAndRates(infoAndRatesEntity: InfoAndRatesEntity){
        insertInfo(infoAndRatesEntity.info)
        insertRates(infoAndRatesEntity.rates)
    }

    @Transaction
    @Query("select * from rates_info where base_code = :baseCode limit 1")
    fun getInfoAndRatesObs(baseCode: String) : LiveData<InfoAndRatesEntity?>

    @Query("select * from rates where info_base_code  =:baseCode")
    fun getRates(baseCode: String) : List<RateEntity>

    @Query("select * from rates where info_base_code  =:baseCode")
    fun getRatesObs(baseCode: String) : LiveData<List<RateEntity>>

    @Query("select * from rates where info_base_code = :baseCode and name like '%' || :searchQuery || '%'")
    fun searchGetRatesObs(baseCode: String, searchQuery: String) : LiveData<List<RateEntity>>

    @Transaction
    @Query("select * from rates_info where base_code = :baseCode limit 1")
    suspend fun getInfoAndRates(baseCode: String) : InfoAndRatesEntity?

    @Query("delete from rates_info")
    suspend fun clearDatabase()

}
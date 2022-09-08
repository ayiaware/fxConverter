package com.ayia.fxconverter.data

import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.ayia.fxconverter.R
import com.ayia.fxconverter.data.local.LocalRepository
import com.ayia.fxconverter.data.remote.RemoteRepository
import com.ayia.fxconverter.domain.mappers.toDomain
import com.ayia.fxconverter.domain.mappers.toEntity
import com.ayia.fxconverter.domain.models.Rate
import com.ayia.fxconverter.util.BASE_TAG
import com.ayia.fxconverter.util.hasInternetConnection
import timber.log.Timber

class AppRepository(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val connectivityManager: ConnectivityManager
) {

    private val TAG = BASE_TAG + " " + AppRepository::class.simpleName

    fun getRatesObs(base: String): LiveData<List<Rate>> {
        return localRepository.getRatesObs(base).map { rates ->
            // Timber.tag(TAG).d("$rates ")
            rates.map { it.toDomain() }
        }
    }

    fun searchGetRatesObs(baseCode: String, searchQuery: String): LiveData<List<Rate>> {

        return localRepository.searchGetRatesObs(baseCode, searchQuery).map { rates ->

            Timber.tag(TAG).d("ratesSize ${rates.size}")
            rates.map { it.toDomain() }

        }
    }


    suspend fun getInfoAndRates(isCachedEmpty: Boolean, base: String, isObs: Boolean): Resource<List<Rate>> {

        Timber.tag(TAG).d("getInfoAndRates isCachedEmpty $isCachedEmpty")

        return if (hasInternetConnection(connectivityManager)) {
            updateInfoAndRatesFromRemote(isCachedEmpty, base, isObs)
        } else {
            Timber.tag(TAG).d("no Connection")
            // delay(1000)
            val msgId = R.string.msg_not_internet

            if (isCachedEmpty)
                Resource.Empty(msgId)
            else
                Resource.Error(msgId)
        }
    }


    private suspend fun updateInfoAndRatesFromRemote(
        isCachedEmpty : Boolean,
        base: String,
        isObs: Boolean
    ): Resource<List<Rate>> {

        Timber.tag(TAG).d("updateInfoAndRatesFromRemote")

        return try {

            val response = remoteRepository.fetchRates(base)

            val result = response.body()

            Timber.tag(TAG).d("Response successful $result ")

            if (response.isSuccessful && result != null) {

                localRepository.insertInfoAndRates(result.toEntity())

                if (isObs) {
                    Resource.Loading
                }
                else {
                    //Used by converter
                    Resource.Success(result.toDomain().rates)
                }

            } else {
                Timber.tag(TAG).d("Response failed $response ${response.message()}")
                val msgId = R.string.msg_network_error

                if (isCachedEmpty)
                    Resource.Empty(msgId)
                else
                    Resource.Error(msgId)
            }

        } catch (e: Exception) {

            Timber.tag(TAG).e(e.message ?: "An error occurred")

            val msgId = R.string.msg_unexpected_error

            if (isCachedEmpty)
                Resource.Empty(msgId)
            else
                Resource.Error(msgId)

        }

    }

    suspend fun clearDatabase() = localRepository.clearDatabase()

    suspend fun getInfo(base: String) = localRepository.getInfo(base)?.toDomain()
    fun getRates(base: String) = localRepository.getRates(base).map { it.toDomain() }
    fun getInfoObs(baseCode: String) = localRepository.getInfoObs(baseCode).map { it?.toDomain() }
    fun getRateObs(code: String, baseCode: String) =
        localRepository.getRateObs(code, baseCode).map { it?.toDomain() }


}
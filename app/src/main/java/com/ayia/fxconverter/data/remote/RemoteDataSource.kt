package com.ayia.fxconverter.data.remote

import com.ayia.fxconverter.data.remote.dto.InfoAndRatesResponse

import retrofit2.Response

interface RemoteDataSource {

    suspend fun fetchRates(base: String): Response<InfoAndRatesResponse>

}
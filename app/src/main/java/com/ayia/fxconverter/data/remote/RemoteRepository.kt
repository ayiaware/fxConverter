package com.ayia.fxconverter.data.remote

import com.ayia.fxconverter.data.remote.dto.InfoAndRatesResponse
import retrofit2.Response

class RemoteRepository(val api: ConversionRatesApi) : RemoteDataSource{
    override suspend fun fetchRates(base: String): Response<InfoAndRatesResponse> = api.getRates(base)
}
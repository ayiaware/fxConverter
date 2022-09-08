package com.ayia.fxconverter.data.remote

import com.ayia.fxconverter.data.remote.dto.InfoAndRatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ConversionRatesApi {

    @GET("latest/{base}")
    suspend fun getRates(
       @Path("base") base:String
    ): Response<InfoAndRatesResponse>

}
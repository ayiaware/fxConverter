package com.ayia.fxconverter.network

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber


//Interceptors, are a powerful mechanism that can monitor, rewrite, and retry the API call
//Another use-case is caching the response of network calls to build an offline-first app
//https://www.geeksforgeeks.org/okhttp-interceptor-in-android/


class HttpRequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
//        First, we obtain the request from the chain. request()
//        The response from the server is then obtained by passing the request through the chain.
//        Proceed(request) At this point, we can check for the response code and take action.
//        Pass this and wait for the results
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder().url(originalRequest.url).build()
        Timber.d(request.toString())

        val response = chain.proceed(request)

        when (response.code) {
            400 -> {
                // Show Bad Request Error Message
            }
            401 -> {
                // Show UnauthorizedError Message
            }

            403 -> {
                // Show Forbidden Message
            }

            404 -> {
                // Show NotFound Message
            }

            // ... and so on
        }
        return response
    }
}
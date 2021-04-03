package com.dlgsoft.mcinema.network

import com.dlgsoft.mcinema.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor : Interceptor {

    /**
     * Intercepts and adds the api key to the request's list of headers
     **/
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.run {
            proceed(
                request()
                    .newBuilder()
                    .addHeader("Authorization", BuildConfig.API_KEY)
                    .build()
            )
        }
    }
}
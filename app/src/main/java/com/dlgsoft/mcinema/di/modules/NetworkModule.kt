package com.dlgsoft.mcinema.di.modules

import com.dlgsoft.mcinema.BuildConfig
import com.dlgsoft.mcinema.api.MCinemaApi
import com.dlgsoft.mcinema.network.RequestInterceptor
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Singleton
    @Provides
    fun provideRequestInterceptor() =
        RequestInterceptor()

    @Singleton
    @Provides
    fun provideFlipperNetworkPlugin() = NetworkFlipperPlugin()

    @Singleton
    @Provides
    fun provideOkHttpBuilder(
        requestInterceptor: RequestInterceptor,
        networkPlugin: NetworkFlipperPlugin
    ): OkHttpClient {
        return OkHttpClient()
            .newBuilder().apply {
                addNetworkInterceptor(FlipperOkhttpInterceptor(networkPlugin))
                //addInterceptor(requestInterceptor)
            }.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        gson: Gson,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideMCinemaApi(retrofit: Retrofit): MCinemaApi =
        retrofit.create(MCinemaApi::class.java)
}

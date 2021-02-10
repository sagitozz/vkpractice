package com.vkpractice.app.di.modules

import com.vkpractice.app.data.database.VkTokenProvider
import com.vkpractice.app.data.network.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @autor d.snytko
 */
@Module
interface NetworkModule {

    @Singleton
    @Binds
    fun provideNetworkService(impl: NetworkServiceImpl): NetworkService

    @Singleton
    @Binds
    fun provideNetworkMonitor(impl: NetworkMonitorImpl): NetworkMonitor

    companion object {

        @Provides
        @Singleton
        fun provideClient(vkTokenProvider: VkTokenProvider): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val httpUrl = chain.request().url.newBuilder()
                    .addQueryParameter(ApiConstants.ACCESS_TOKEN, vkTokenProvider.getToken())
                    .addQueryParameter(ApiConstants.VERSION, ApiConstants.VERSION_NUMBER)
                    .build()

                chain.proceed(chain.request().newBuilder().url(httpUrl).build())
            })
            .readTimeout(ApiConstants.TIMEOUT_IN_SECOND.toLong(), TimeUnit.SECONDS)
            .connectTimeout(ApiConstants.TIMEOUT_IN_SECOND.toLong(), TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor())
            .build()

        @Provides
        @Singleton
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        @Provides
        @Singleton
        fun provideRestApi(retrofit: Retrofit): VkRestApi = retrofit.create(VkRestApi::class.java)
    }
}


package com.breez.testapplication.di

import com.breez.testapplication.BuildConfig
import com.breez.testapplication.dataFlow.network.Api
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideApiInterface(retrofit: Retrofit): Api = retrofit.create(Api::class.java)

    @Singleton
    @Provides
    fun provideRetrofit(httpUrl: HttpUrl,
                        client: OkHttpClient,
                        gson: Gson): Retrofit {
        return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
    }

    @Singleton
    @Provides
    fun provideHttpUrl(): HttpUrl = HttpUrl.parse(BuildConfig.API_BASE_URL)!!

    @Singleton
    @Provides
    fun provideOkHttpClient(loggerInterceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor {
                val original = it.request()
                val request = original.newBuilder()
                        .header("Authorization", "Bearer ${BuildConfig.TOKEN}")
                        .method(original.method(), original.body())
                        .build()
                it.proceed(request)
            }
            .addNetworkInterceptor(loggerInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .protocols(Collections.singletonList(Protocol.HTTP_1_1))
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(40, TimeUnit.SECONDS)
            .build()

    @Singleton
    @Provides
    fun provideHttpLoggerInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Timber.tag(NETWORK_TAG).d(it)
        })
        logger.level = HttpLoggingInterceptor.Level.BODY

        return logger
    }

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder()
            .serializeNulls()
            .create()

    companion object {
        private const val NETWORK_TAG = "API_NETWORK_PROVIDER"
    }
}
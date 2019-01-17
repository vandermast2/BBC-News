package com.breez.testapplication.di

import android.app.Application
import android.content.Context
import com.breez.testapplication.dataFlow.DataManger
import com.breez.testapplication.dataFlow.IDataManager
import com.breez.testapplication.dataFlow.network.Api
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [NetworkModule::class])
class AppModule(private val application: Application) {
    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun provideDataManager(api: Api): IDataManager = DataManger(api)
}
package com.breez.testapplication.di

import com.breez.testapplication.AppApplication
import com.breez.testapplication.ui.base.BaseNVM
import dagger.Component
import javax.inject.Singleton
import com.google.gson.annotations.SerializedName

@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {
    fun inject(application: AppApplication)

    fun inject(baseNVM: BaseNVM)
}
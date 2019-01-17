package com.breez.testapplication.dataFlow.network

import com.breez.testapplication.dataFlow.network.model.EverythingNewsModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("v2/top-headlines")
    fun getTopNews(@Query("q") q:String) :Deferred<EverythingNewsModel>
}
package com.breez.testapplication.dataFlow

import com.breez.testapplication.dataFlow.network.Api
import com.breez.testapplication.dataFlow.network.model.EverythingNewsModel
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class DataManger @Inject constructor(private val api: Api) : IDataManager {
    override fun getTop(): Deferred<EverythingNewsModel> = api.getTopNews("all")

}
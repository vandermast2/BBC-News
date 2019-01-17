package com.breez.testapplication.dataFlow

import com.breez.testapplication.dataFlow.network.model.EverythingNewsModel
import kotlinx.coroutines.Deferred

interface IDataManager {
    fun getTop():Deferred<EverythingNewsModel>
}
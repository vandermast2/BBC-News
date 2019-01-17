package com.breez.testapplication.ui.fragments.list

import androidx.lifecycle.MutableLiveData
import com.breez.testapplication.dataFlow.network.model.EverythingNewsModel
import com.breez.testapplication.dataFlow.network.model.Response
import com.breez.testapplication.dataFlow.network.model.Status
import com.breez.testapplication.ui.base.BaseNVM

class ListViewModel : BaseNVM() {
    private val topNews: MutableLiveData<Response<EverythingNewsModel>> = object : MutableLiveData<Response<EverythingNewsModel>>() {
        override fun onActive() {
            super.onActive()
            getTopNews()
        }
    }

    fun news() = topNews

    private fun getTopNews() {
        processAsyncProviderCall(
            call = {  dataManager.getTop() },
            onSuccess = {
                topNews.postValue(Response(Status.SUCCESS, it, null))
            },
            onError = {
                topNews.postValue(Response(Status.ERROR, null, it))
                onError(it)
            },
            showProgress = true
        )
    }
}

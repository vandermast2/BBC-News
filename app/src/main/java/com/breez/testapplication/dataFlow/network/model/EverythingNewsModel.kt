package com.breez.testapplication.dataFlow.network.model

import com.google.gson.annotations.SerializedName

data class EverythingNewsModel(
    @SerializedName("articles")
    var articles: List<Article?>?,
    @SerializedName("status")
    var status: String?, // ok
    @SerializedName("totalResults")
    var totalResults: Int? // 4893
)


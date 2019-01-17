package com.breez.testapplication.dataFlow.network.model

import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("author")
    var author: String?, // Kate Clark
    @SerializedName("content")
    var content: String?, // The Intercontinental Exchange’s (ICE) cryptocurrency project Bakkt celebrated New Year’s Eve with the announcement of a $182.5 million equity round from a slew of notable institutional investors. ICE, the operator of several global exchanges, including the Ne… [+3472 chars]
    @SerializedName("description")
    var description: String?, // Venture capitalists remain bullish on Bitcoin and its underlying technology despite sinking crypto prices.
    @SerializedName("publishedAt")
    var publishedAt: String?, // 2018-12-31T18:56:47Z
    @SerializedName("source")
    var source: Source?,
    @SerializedName("title")
    var title: String?, // NYSE operator’s crypto project Bakkt brings in $182M
    @SerializedName("url")
    var url: String?, // http://techcrunch.com/2018/12/31/nyse-operators-crypto-project-bakkt-brings-in-182m/
    @SerializedName("urlToImage")
    var urlToImage: String? // https://techcrunch.com/wp-content/uploads/2018/12/GettyImages-1064373142.jpg?w=600
)
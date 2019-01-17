package com.breez.testapplication.dataFlow.network.model

import com.google.gson.annotations.SerializedName

data class Source(
    @SerializedName("id")
    var id: String?, // techcrunch
    @SerializedName("name")
    var name: String? // TechCrunch
)
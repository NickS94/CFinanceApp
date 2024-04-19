package com.example.cfinanceapp.data.models

import com.squareup.moshi.Json

data class USData(

    @Json(name = "volume_24h")
    val volume24h : Double,

    val price : Double,
    @Json(name = "percent_change_1h")
    val percentChange1h: Double,
    @Json(name = "percent_change_24h")
    val percentChange24h: Double,
    @Json(name = "percent_change_7d")
    val percentChange7d: Double,
    @Json(name = "market_cap")
    val marketCap : Double
)

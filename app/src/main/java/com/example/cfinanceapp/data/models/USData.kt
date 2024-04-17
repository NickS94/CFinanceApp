package com.example.cfinanceapp.data.models

import com.squareup.moshi.Json

data class USData(
    val price : Int,
    @Json(name = "percent_change_1h")
    val percentChange1h: Double,
    @Json(name = "percent_change_24h")
    val percentChange24h: Double,
    @Json(name = "percent_change_7d")
    val percentChange7d: Double,
)

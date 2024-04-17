package com.example.cfinanceapp.data.models

import com.squareup.moshi.Json


data class Quote(
    @Json(name = "USD")
    val usdData: USData,


    )

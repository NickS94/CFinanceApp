package com.example.cfinanceapp.data.models

import com.squareup.moshi.Json
import kotlinx.serialization.Serializable

@Serializable
data class Quote(
    @Json(name = "USD")
    val usdData: USData,


    )

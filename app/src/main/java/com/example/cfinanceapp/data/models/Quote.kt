package com.example.cfinanceapp.data.models

import androidx.room.TypeConverters
import com.example.cfinanceapp.tools.USDataConverter
import com.squareup.moshi.Json
import kotlinx.serialization.Serializable

@Serializable
@TypeConverters(USDataConverter::class)
data class Quote(
    @Json(name = "USD")
    val usdData: USData,


    )

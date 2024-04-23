package com.example.cfinanceapp.data.models

import androidx.room.TypeConverters
import com.example.cfinanceapp.tools.USDataConverter
import com.squareup.moshi.Json


data class Quote(
    @Json(name = "USD")
    @TypeConverters(USDataConverter::class)
    val usdData: USData,


    )

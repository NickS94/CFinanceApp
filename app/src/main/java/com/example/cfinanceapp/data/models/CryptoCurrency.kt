package com.example.cfinanceapp.data.models

import com.squareup.moshi.Json

data class CryptoCurrency(

    val id: Int,
    val name: String,
    val symbol: String,

    @Json(name = "cmc_rank")
    val cmcRank: Int,

    @Json(name = "circulating_supply")
    val circulatingSupply: Double,

    @Json(name = "total_supply")
    val totalSupply: Double,

    @Json(name = "max_supply")
    val maxSupply: Double,

    val quote: Quote
)

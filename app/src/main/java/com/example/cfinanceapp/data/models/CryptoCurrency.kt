package com.example.cfinanceapp.data.models

import com.squareup.moshi.Json

data class CryptoCurrency(
    val name: String,
    val id: Int,
    @Json(name = "cmc_rank")
    val rank: Int,
    @Json(name = "total_supply")
    val totalSupply: Double,
    @Json(name = "circulating_supply")
    val circulatingSupply: Double,
    @Json(name = "max_supply")
    val maxSupply: Double?,

    val symbol: String,
    val quote: Quote

)

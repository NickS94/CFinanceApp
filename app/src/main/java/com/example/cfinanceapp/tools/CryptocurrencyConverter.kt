package com.example.cfinanceapp.tools

import androidx.room.TypeConverter
import com.example.cfinanceapp.data.models.CryptoCurrency
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CryptocurrencyConverter {

    @OptIn(ExperimentalSerializationApi::class)
    @TypeConverter
    fun cryptocurrencyToJson(cryptoCurrency: CryptoCurrency): String =
        Json.encodeToString(cryptoCurrency)

    @OptIn(ExperimentalSerializationApi::class)
    @TypeConverter
    fun jsonToCryptoCurrency(value: String): CryptoCurrency =
        Json.decodeFromString<CryptoCurrency>(value)


}
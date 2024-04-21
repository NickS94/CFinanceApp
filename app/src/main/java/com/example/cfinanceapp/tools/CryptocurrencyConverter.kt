package com.example.cfinanceapp.tools

import androidx.room.TypeConverter
import com.example.cfinanceapp.data.models.CryptoCurrency
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CryptocurrencyConverter {
    @TypeConverter
    fun cryptocurrencyToJson(cryptoCurrency: CryptoCurrency):String = Json.encodeToString(cryptoCurrency)
    @TypeConverter
    fun jsonToCryptoCurrency(value: String): CryptoCurrency = Json.decodeFromString<CryptoCurrency>(value)





}
package com.example.cfinanceapp.tools

import androidx.room.TypeConverter
import com.example.cfinanceapp.data.models.Quote
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class QuoteConverter {
    @TypeConverter
    fun quoteToString(quote: Quote): String {
        return Json.encodeToString(quote)
    }

    @TypeConverter
    fun stringToQuote(value: String): Quote {
        return Json.decodeFromString(value)
    }
}

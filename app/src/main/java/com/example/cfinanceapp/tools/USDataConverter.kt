package com.example.cfinanceapp.tools

import androidx.room.TypeConverter
import com.example.cfinanceapp.data.models.USData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class USDataConverter {
    @TypeConverter
    fun usDataToString(usData: USData): String {
        return Json.encodeToString(usData)
    }

    @TypeConverter
    fun stringToUSData(value: String): USData {
        return Json.decodeFromString(value)
    }
}
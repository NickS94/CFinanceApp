package com.example.cfinanceapp.tools

import androidx.room.TypeConverter
import com.example.cfinanceapp.data.models.Asset
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AssetConverter {
    @TypeConverter
    fun assetToJson(asset: Asset):String = Json.encodeToString(asset)
    @TypeConverter
        fun jsonToCryptoAsset(value: String): Asset = Json.decodeFromString<Asset>(value)
}
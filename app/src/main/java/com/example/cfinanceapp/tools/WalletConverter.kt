package com.example.cfinanceapp.tools

import androidx.room.TypeConverter
import com.example.cfinanceapp.data.models.CryptoCurrency
import com.example.cfinanceapp.data.models.Wallet
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WalletConverter {


    @TypeConverter
    fun walletToJson(wallet: Wallet):String = Json.encodeToString(wallet)
    @TypeConverter
    fun jsonToWallet(value: String): Wallet = Json.decodeFromString<Wallet>(value)

}
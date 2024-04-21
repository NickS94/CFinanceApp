package com.example.cfinanceapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cfinanceapp.tools.CryptocurrencyConverter



@Entity
data class Asset(
    @PrimaryKey(autoGenerate = true)
    val walletId: Long,
    @TypeConverters(CryptocurrencyConverter::class)
    val crypto: CryptoCurrency
)
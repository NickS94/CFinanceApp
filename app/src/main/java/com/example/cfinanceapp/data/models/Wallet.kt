package com.example.cfinanceapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cfinanceapp.tools.QuoteConverter

@Entity(tableName = "Wallet")

data class Wallet(
    @PrimaryKey
    val id : Long = 0,
    @ColumnInfo("assets")
    @TypeConverters(CryptoCurrency::class)
    var assets : MutableList<CryptoCurrency> = mutableListOf(),
    @ColumnInfo("transactions")
    val transactionHash : String,
    val accountId : Long
)

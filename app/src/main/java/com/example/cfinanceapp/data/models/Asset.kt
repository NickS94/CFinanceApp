package com.example.cfinanceapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cfinanceapp.tools.CryptocurrencyConverter

@Entity(tableName ="Assets")
data class Asset(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    @ColumnInfo(name = "Fiat")
    val fiat :Double?,
    @ColumnInfo(name = "CryptoCurrency")
    @TypeConverters(CryptocurrencyConverter::class)
    val cryptoCurrency: CryptoCurrency,
    @ColumnInfo(name = "Amount")
    val amount :Double,
    @ColumnInfo(name = "TransactionHash")
    val transactionHash :String,
    @ColumnInfo(name = "Date")
    val transactionDate :String,

    val walletId:Long

)

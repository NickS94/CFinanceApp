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
    val fiat :String?,
    @ColumnInfo(name = "CryptoCurrency")
    val cryptoCurrency: CryptoCurrency?,
    @ColumnInfo(name = "Amount")
    var amount :Double,

    val walletId:Long

)

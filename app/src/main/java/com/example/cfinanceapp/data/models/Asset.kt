package com.example.cfinanceapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName ="Assets")
data class Asset(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    @ColumnInfo(name = "Fiat")
    val fiat :String?,
    @ColumnInfo(name = "CryptoCurrency")
    var cryptoCurrency: CryptoCurrency?,
    @ColumnInfo(name = "Amount")
    var amount :Double,

    val walletId:Long

)

package com.example.cfinanceapp.data.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(
    foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = ["accountId"],
        childColumns = ["walletId"],
        onDelete = ForeignKey.CASCADE
    )]
)

data class Wallet(
    @PrimaryKey
    val walletId: Long = 0,
    @Embedded
    val transactions: Transactions,
    val accountId:Long,
    val assets:List<CryptoCurrency>


)

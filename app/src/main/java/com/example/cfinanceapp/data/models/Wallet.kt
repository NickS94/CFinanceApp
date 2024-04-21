package com.example.cfinanceapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverters
import com.example.cfinanceapp.tools.AssetConverter

@Entity(
    foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = ["accountId"],
        childColumns = ["walletId"],
        onDelete = ForeignKey.CASCADE
    )]
)
@TypeConverters(AssetConverter::class)
data class Wallet(
    @PrimaryKey
    val walletId: Long = 0,
    @Embedded
    val transactions: Transactions, // Embedded Transactions object
    // Define a different column name for walletId in Transactions object
    val accountId:Long,

    val assets:Asset

    // Other fields in Wallet entity
)

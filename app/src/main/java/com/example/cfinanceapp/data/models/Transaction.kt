package com.example.cfinanceapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    @ColumnInfo("Symbol")
    val symbol : String,
    @ColumnInfo("Amount")
    var amount : Double,
    @ColumnInfo("Price")
    val price : Double?,
    @ColumnInfo("Date")
    val date : String,
    @ColumnInfo("Transaction_Hash")
    val transactionHash :String,
    @ColumnInfo("Status")
    val isBought : Boolean?,

    val walletId : Long

)
